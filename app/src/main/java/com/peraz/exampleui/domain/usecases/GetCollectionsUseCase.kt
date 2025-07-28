package com.peraz.exampleui.domain.usecases

import com.peraz.exampleui.data.CollectionRepository
import com.peraz.exampleui.data.local.CollectionsEntity
import com.peraz.exampleui.domain.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class GetCollectionsUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
)  {
    operator fun invoke(): Flow<Resource<List<CollectionsEntity>>> = flow{
        try {
            emit(Resource.Loading())
            emit(Resource.Success(
                data = collectionRepository.refreshCollection()
            ))
        }catch (e: IOException){
            emit(Resource.Error(message= e.message ?: "Unknow error" ))
        }
    }
}