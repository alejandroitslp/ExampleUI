package com.peraz.exampleui.presentation.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peraz.exampleui.data.CollectionRepository
import com.peraz.exampleui.data.ProductRepository
import com.peraz.exampleui.data.local.toModel
import com.peraz.exampleui.data.remote.ColProductModel
import com.peraz.exampleui.data.remote.CollectionsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {
    var collections = mutableStateListOf<CollectionsModel>()
    var products = mutableStateListOf<ColProductModel>()

    init {

        refreshCollections()
        refreshProducts()

    }
    fun refreshProducts(
        id: Int? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var conversion: ColProductModel? = null
            try {
                productRepository.refreshProducts(id)
            } catch (e: Exception) {
                Log.d("HomeScreenViewModel", "${e.message}")
            } //Dependiendo de all lo que se guardo en la base de datos, regresa la coleccion hecha Modelo.
            if (id != null) {
                products.clear()
                    productRepository.getSpecificCollectionProducts(id).map {
                        it.toModel()
                        products.add(it.toModel())
                    }
            } else {
                products.clear()
                    productRepository.getProducts().map{
                        it.toModel()
                        products.add(it.toModel())
                    }
            }
            return@launch
        }
    }

    fun refreshCollections() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                collectionRepository.refreshCollection()
            } catch (e: Exception) {
                Log.d("HomeScreenViewModel", "${e.message}")
            }//Se llama a jalar los datos a la base de datos.
            collections.clear()
                collectionRepository.getCollections().map{
                    it.toModel()
                    collections.add(it.toModel())
                }
        }//Dependiendo de all lo que se guardo en la base de datos, regresa la coleccion hecha Modelo.
    }

}