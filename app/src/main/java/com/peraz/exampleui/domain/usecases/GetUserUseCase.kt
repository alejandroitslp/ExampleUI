package com.peraz.exampleui.domain.usecases

import com.peraz.exampleui.data.LoginRepository
import com.peraz.exampleui.data.remote.UserModel
import com.peraz.exampleui.domain.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class GetUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository
)  {
    operator fun invoke(name: String?, password: String?): Flow<Resource<UserModel?>> = flow{
        try {
            emit(Resource.Loading())
            emit(Resource.Success(
                data = loginRepository.getUser(name, password)
            ))
        }catch (e: IOException){
            emit(Resource.Error(message= e.message ?: "Unknow error" ))
        }
    }
}