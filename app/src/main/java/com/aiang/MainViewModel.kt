package com.aiang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.preferences.UserModel
import com.aiang.data.repository.Repository
import com.aiang.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<UserModel>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<UserModel>> get() = _uiState

    fun getSession() {
        viewModelScope.launch {
            repository.getSession()
                .catch { _uiState.value = UiState.Error(it.message.toString()) }
                .collect { _uiState.value = UiState.Success(it)}
        }
    }
}