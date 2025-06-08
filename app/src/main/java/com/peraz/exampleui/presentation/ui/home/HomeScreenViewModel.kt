package com.peraz.exampleui.presentation.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peraz.exampleui.data.CollectionsModel
import com.peraz.exampleui.data.Resultado
import com.peraz.exampleui.domain.Resource
import com.peraz.exampleui.domain.usecases.GetCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getCollectionsUseCase: GetCollectionsUseCase
): ViewModel() {

    val collections= mutableStateListOf<Resultado>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val collections = getCollectionsUseCase.invoke().collect { result ->
                if (result is Resource.Success) {
                    if (result.data?.resultado != null) {
                        collections.addAll(result.data.resultado)
                    }
                }
            }
        }
    }
}