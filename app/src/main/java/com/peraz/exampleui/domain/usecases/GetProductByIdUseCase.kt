package com.peraz.exampleui.domain.usecases

import com.peraz.exampleui.data.ProductRepository
import com.peraz.exampleui.data.local.ProductsEntity
import com.peraz.exampleui.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
)  {

    operator fun invoke(id: Int): Flow<Resource<ProductsEntity>> = flow{
        try {
            emit(Resource.Loading())
            emit(Resource.Success(
                data = productRepository.getProductById(id)
            ))
        }catch (e: IOException){
            emit(Resource.Error(message= e.message ?: "Unknow error" ))
        }
    }
}