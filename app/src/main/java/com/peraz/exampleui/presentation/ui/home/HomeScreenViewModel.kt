package com.peraz.exampleui.presentation.ui.home

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peraz.exampleui.data.CollectionRepository
import com.peraz.exampleui.data.ProductRepository
import com.peraz.exampleui.data.UserPreferencesRepository
import com.peraz.exampleui.data.local.toModel
import com.peraz.exampleui.data.remote.ColProductModel
import com.peraz.exampleui.data.remote.CollectionsModel
import com.peraz.exampleui.domain.Resource
import com.peraz.exampleui.domain.usecases.GetAllProductsUseCase
import com.peraz.exampleui.domain.usecases.GetCollectionsUseCase
import com.peraz.exampleui.domain.usecases.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val productRepository: ProductRepository,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    var _collections = mutableStateListOf<CollectionsModel>()
    val collections =_collections
    val products = mutableStateListOf<ColProductModel>()
    var _isLoading= mutableStateOf(false)
    val isLoading = _isLoading
    var progresBar= mutableFloatStateOf(0f)
    var _progressBar = getAllProductsUseCase.progress
    var _error = MutableSharedFlow<String>()
    val error= _error.asSharedFlow()
    var _productById= mutableListOf<ColProductModel>()
    var productById=_productById
    var _name=mutableStateOf<String?>("")
    var _token=mutableStateOf<String?>("")
    var _role=mutableStateOf<Int?>(0)

    val name=_name
    val token=_token
    val role=_role


    init {
        viewModelScope.launch {
            userPreferencesRepository.userNameFlow.collect { name ->
                _name.value = name
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.roleFlow.collect { role ->
                _role.value = role
            }
        }
        viewModelScope.launch {
        userPreferencesRepository.tokenFlow.collect {
                token->
                _token.value=token
            }
        }


        viewModelScope.launch {
            try {
                getCollectionsUseCase.invoke().collect {
                        response->
                    if (response is Resource.Success){
                        isLoading.value=true
                        refreshCollectionsDao()
                    }
                    if (response is Resource.Loading){
                        isLoading.value=false
                    }
                }
            }catch(e: IOException){
                Log.d("FallaCollection","$e")
            }
        }

        getProductsFromRetrofit()


    }

    fun getProductsFromRetrofit(){
        viewModelScope.launch {
            try {
                getAllProductsUseCase.invoke().collect {
                        response->
                    if (response is Resource.Success){
                        refreshProductsDao()
                    }
                    if (response is Resource.Loading){
                        viewModelScope.launch(Dispatchers.IO) {
                            _progressBar.collect {
                                    resultado->
                                progresBar.floatValue=resultado
                            }
                        }
                    }
                    if (response is Resource.Error){
                        //Aqui se puede poner la falla de conexion y que envie dato para decir que
                        //se trabajara con la base de datos local.
                        refreshProductsDao()
                        _error.emit("Algo anduvo mal con la conexion a Datos, recuperando de base de datos local")
                    }
                }
            }catch(e: IOException){
                Log.d("FallaProduct","$e")
            }
        }
    }

    fun refreshProductsDao(
        id: Int? = null,
    ) {
        products.clear()
        val job1=viewModelScope.launch(Dispatchers.IO) {
            if (id != null) {
                productRepository.getSpecificCollectionProducts(id)
            } else{
                productRepository.getProducts()
            }
            products.addAll(productRepository.listOfProductsModel)
        }
    }

    fun refreshCollectionsDao() {
        var job2= viewModelScope.launch(Dispatchers.IO) {
            collections.clear()
                collectionRepository.getCollections().map{
                    it.toModel()
                    collections.add(it.toModel())
                }
        }//Dependiendo de all lo que se guardo en la base de datos, regresa la coleccion hecha Modelo.
    }

    fun getProductsById(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            getProductByIdUseCase.invoke(id).collect {
                result->

                if (result.data!=null){
                    _productById.clear()
                    var model=result.data.toModel()
                    _productById.add(result.data.toModel())
                    Log.d("HomeScreenProductById", "$id , ${result.data}")
                }
                else{
                    return@collect
                }
            }
        }
    }

    fun resetUser(){
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateUserName("")
            userPreferencesRepository.updateToken("")
            userPreferencesRepository.updateRole(0)
        }
    }

    fun sendWhatsappMessage(
        context: Context,
        name: String,
        price: String,
        payments: String,
        imageFile: File
    ){
        val fileUri: Uri? = try {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}",
                imageFile
            )
        }catch (e: IllegalArgumentException){
            Log.d("HSVM","Error creando la uri:  ${e.message}")
            null
        }

        fileUri?.let { uri->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                Log.d("fileUri","$uri")
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Nombre del producto: $name , precio normal: $${price}, el precio a pagos solicitado: $${payments}")
                setPackage("com.whatsapp")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            try {
                context.startActivity(intent)
            }catch (e: ActivityNotFoundException){
                Toast.makeText(context, "Whatsapp no instalado", Toast.LENGTH_SHORT).show()
            }
        }

    }

}