package com.aiang.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.preferences.UserModel
import com.aiang.data.repository.Repository
import com.aiang.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(
        UiState.Waiting)
    val uiState: StateFlow<UiState<Boolean>> get() = _uiState

    private lateinit var user: UserModel

    fun getSession() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.getSession().collect { userData ->
                user = UserModel(
                    userId = userData.userId,
                    email = userData.email,
                    token = userData.token,
                    isFormFilled = userData.isFormFilled
                )
                Log.i("HomeViewModel", user.isFormFilled.toString())
                _uiState.value = UiState.Success(userData.isFormFilled)
            }
        }
    }
}