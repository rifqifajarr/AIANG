package com.aiang.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.api.ApiConfig
import com.aiang.data.api.response.LoginResponse
import com.aiang.data.api.response.RegisterResponse
import com.aiang.data.preferences.UserModel
import com.aiang.data.repository.Repository
import com.aiang.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: Repository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<LoginResponse>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<LoginResponse>> get() = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val client = ApiConfig.getApiService().login(email, password)
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _uiState.value = UiState.Waiting
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        _uiState.value = UiState.Success(responseBody)
                    } else {
                        _uiState.value = UiState.Error(response.message())
                        Log.e("LoginViewModel", "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _uiState.value = UiState.Error(t.message.toString())
                    Log.e("LoginViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}