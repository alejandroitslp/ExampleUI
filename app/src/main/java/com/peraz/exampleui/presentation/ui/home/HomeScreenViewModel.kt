package com.peraz.exampleui.presentation.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.peraz.exampleui.data.CollectionRepository
import com.peraz.exampleui.data.ProductRepository
import com.peraz.exampleui.data.UserPreferencesRepository
import com.peraz.exampleui.data.local.ProductsEntity
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
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
        viewModelScope.launch{
            userPreferencesRepository.userNameFlow.collect{
                name->
                _name.value=name
            }
            userPreferencesRepository.roleFlow.collect {
                role->
                _role.value=role
            }
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


}