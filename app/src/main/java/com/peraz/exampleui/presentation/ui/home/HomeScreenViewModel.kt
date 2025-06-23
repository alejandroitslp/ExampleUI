package com.peraz.exampleui.presentation.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peraz.exampleui.data.CollectionRepository
import com.peraz.exampleui.data.ProductRepository
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
    private val getProductByIdUseCase: GetProductByIdUseCase
) : ViewModel() {
    var _collections = mutableStateListOf<CollectionsModel>()
    val collections =_collections
    var _products = mutableStateListOf<ColProductModel>()
    val products = _products
    var _isLoading= mutableStateOf(false)
    val isLoading = _isLoading
    var progresBar= mutableFloatStateOf(0f)
    var _progressBar = getAllProductsUseCase.progress
    var _error = MutableSharedFlow<String>()
    val error= _error.asSharedFlow()
    var _productById= mutableListOf<ColProductModel>()
    var productById=_productById


    init {
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
        var job1=viewModelScope.launch(Dispatchers.IO) {
            if (id != null) {
                products.clear()
                    productRepository.getSpecificCollectionProducts(id).map {
                        it.toModel()
                        products.add(it.toModel())
                    }
            } else{
                products.clear()
                    productRepository.getProducts().map{
                        it.toModel()
                        products.add(it.toModel())
                    }
            }
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