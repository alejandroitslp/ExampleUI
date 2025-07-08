package com.peraz.exampleui.presentation.ui.details

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peraz.exampleui.data.ProductRepository
import com.peraz.exampleui.data.remote.ColProductModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel(){
    var products = mutableStateListOf<ColProductModel>()

    init {
        refreshProductsDao()
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
}