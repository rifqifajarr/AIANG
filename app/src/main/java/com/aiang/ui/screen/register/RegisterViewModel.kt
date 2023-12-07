package com.aiang.ui.screen.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.api.ApiConfig
import com.aiang.data.api.response.RegisterResponse
import com.aiang.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<RegisterResponse>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<RegisterResponse>> get() = _uiState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val client = ApiConfig.getApiService().register(name, email, password)
            client.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _uiState.value = UiState.Waiting
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        _uiState.value = UiState.Success(responseBody)
                    } else {
                        _uiState.value = UiState.Error(response.message())
                        Log.e("RegisterViewModel", "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _uiState.value = UiState.Error(t.message.toString())
                    Log.e("RegisterViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }
}