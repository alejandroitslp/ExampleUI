package com.peraz.exampleui.presentation.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peraz.exampleui.data.CollectionRepository
import com.peraz.exampleui.data.ProductRepository
import com.peraz.exampleui.data.local.toModel
import com.peraz.exampleui.data.remote.ColProductModel
import com.peraz.exampleui.data.remote.CollectionsModel
import com.peraz.exampleui.domain.Resource
import com.peraz.exampleui.domain.usecases.GetAllProductsUseCase
import com.peraz.exampleui.domain.usecases.GetCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.io.IOException

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val productRepository: ProductRepository,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase
) : ViewModel() {
    var collections = mutableStateListOf<CollectionsModel>()
    var products = mutableStateListOf<ColProductModel>()
    var isLoading= mutableStateOf(false)

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


        viewModelScope.launch {
            try {
                getAllProductsUseCase.invoke().collect {
                    response->
                    if (response is Resource.Success){
                        refreshProductsDao(1)
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
        viewModelScope.launch(Dispatchers.IO) {
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
            return@launch
        }
    }

    fun refreshCollectionsDao() {
        viewModelScope.launch(Dispatchers.IO) {
            collections.clear()
                collectionRepository.getCollections().map{
                    it.toModel()
                    collections.add(it.toModel())
                }
        }//Dependiendo de all lo que se guardo en la base de datos, regresa la coleccion hecha Modelo.
    }

}