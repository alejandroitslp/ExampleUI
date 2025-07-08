package com.peraz.exampleui.presentation.ui.welcome

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.peraz.exampleui.data.LoginRepository
import com.peraz.exampleui.domain.usecases.GetCollectionsUseCase
import com.peraz.exampleui.domain.usecases.GetUserUseCase
import okio.IOException
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.peraz.exampleui.data.remote.UserModel
import com.peraz.exampleui.domain.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    ): ViewModel() {
        var _users = mutableStateListOf<UserModel?>()
        val users = _users
        var _isReady=mutableStateOf(false)
        val isReady=_isReady
        var _isButtonAvailable=mutableStateOf(true)
        var isButtonAvailable=_isButtonAvailable

    fun login(name: String?, password: String?){
        viewModelScope.launch {
            getUserUseCase.invoke(name, password).collect {
                result->
                when(result){
                    is Resource.Loading -> {
                        _isReady.value=false
                    }
                    is Resource.Success -> {
                        _isReady.value=true
                        _users.clear()
                        _users.add(result.data)
                        delay(3000)
                        _isReady.value=false
                    }
                    is Resource.Error -> {
                    }
                }
            }
        }
        try {
        }catch (e: IOException){

        }
    }
}