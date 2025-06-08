package com.peraz.exampleui.domain.usecases

import com.peraz.exampleui.data.ApiInterface
import com.peraz.exampleui.data.CollectionsModel
import com.peraz.exampleui.domain.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class GetCollectionsUseCase @Inject constructor(
    private val repositoryInterface: ApiInterface
)  {
    operator fun invoke(): Flow<Resource<CollectionsModel>> = flow{
        try {
            emit(Resource.Loading())
            emit(Resource.Success(
                data = repositoryInterface.getCollections().body()
            ))
        }catch (e: IOException){
            emit(Resource.Error(message= e.message ?: "Unknow error" ))
        }
    }
}