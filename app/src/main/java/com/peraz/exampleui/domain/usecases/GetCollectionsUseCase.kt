package com.peraz.exampleui.domain.usecases

import com.peraz.exampleui.data.remote.ApiInterface
import com.peraz.exampleui.data.remote.ResponseCollectionsModel
import com.peraz.exampleui.domain.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class GetCollectionsUseCase @Inject constructor(
    private val repositoryInterface: ApiInterface
)  {
    operator fun invoke(): Flow<Resource<ResponseCollectionsModel>> = flow{
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