package com.peraz.exampleui.domain.usecases

import com.peraz.exampleui.data.ProductRepository
import com.peraz.exampleui.data.local.ProductsDao
import com.peraz.exampleui.data.local.ProductsEntity
import com.peraz.exampleui.data.remote.ApiInterface
import com.peraz.exampleui.data.remote.ColProductModel
import com.peraz.exampleui.data.remote.ResponseCollectionsModel
import com.peraz.exampleui.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
    )  {
        operator fun invoke(): Flow<Resource<List<ProductsEntity>>> = flow{
            try {
                emit(Resource.Loading())
                emit(Resource.Success(
                    data = productRepository.refreshProducts()
                ))
            }catch (e: IOException){
                emit(Resource.Error(message= e.message ?: "Unknow error" ))
            }
        }
}