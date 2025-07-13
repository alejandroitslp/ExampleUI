package com.peraz.exampleui.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.peraz.exampleui.data.remote.ApiInterface
import com.peraz.exampleui.data.remote.LoginRequest
import com.peraz.exampleui.data.remote.UserModel
import com.peraz.exampleui.di.DatabaseModule
import kotlinx.coroutines.coroutineScope
import okio.IOException
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val userPreferencesRepository: UserPreferencesRepository
){
    val user= mutableStateListOf<UserModel?>()
    suspend fun getUser(name: String?, password: String?): UserModel?{
        try {
            val user=apiInterface.login(LoginRequest(email = name, password = password)).body()?.user
            if(user!=null){
                userPreferencesRepository.updateUserName(user.fullName)
                userPreferencesRepository.updateRole(user.admin)
                userPreferencesRepository.updateToken(user.token)
            }else{
                userPreferencesRepository.updateUserName("")
                userPreferencesRepository.updateRole(0)
                userPreferencesRepository.updateToken("")
            }
            return user
        } catch (e: IOException) {
            throw e
        }
    }
}