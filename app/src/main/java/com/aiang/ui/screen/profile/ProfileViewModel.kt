package com.aiang.ui.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.api.ApiConfig
import com.aiang.data.api.response.LoginResponse
import com.aiang.data.preferences.UserModel
import com.aiang.data.repository.Repository
import com.aiang.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class ProfileViewModel(private val repository: Repository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<UserModel>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<UserModel>> get() = _uiState

    fun getSession() {
        viewModelScope.launch {
            repository.getSession()
                .catch { _uiState.value = UiState.Error(it.message.toString()) }
                .collect { _uiState.value = UiState.Success(it)}
        }
    }

    fun logout() {
        val client = ApiConfig.getApiService().logout()
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    Log.i("response.isSuccessful", "success")
                }
                else Log.e("LoginViewModel", "onFailure: ${response.errorBody()}")
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun clearPreference() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}