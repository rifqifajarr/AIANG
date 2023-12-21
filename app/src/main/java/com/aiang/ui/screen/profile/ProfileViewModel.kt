package com.aiang.ui.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.api.ApiConfig
import com.aiang.data.api.response.DeleteDailyRoutineResponse
import com.aiang.data.api.response.GetTokenResponse
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
import java.time.LocalDate

class ProfileViewModel(private val repository: Repository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<UserModel>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<UserModel>> get() = _uiState

    private lateinit var user: UserModel

    fun getSession() {
        viewModelScope.launch {
            repository.getSession()
                .catch { _uiState.value = UiState.Error(it.message.toString()) }
                .collect { userData ->
                    user = UserModel(
                        userId = userData.userId,
                        email = userData.email,
                        token = userData.token,
                        finishedTaskId = userData.finishedTaskId
                    )
                    _uiState.value = UiState.Success(userData)
                }
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

    fun getTokenThenResetData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val client = ApiConfig.getApiService().getToken(user.userId)
            client.enqueue(object : Callback<GetTokenResponse> {
                override fun onResponse(
                    call: Call<GetTokenResponse>,
                    response: Response<GetTokenResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        user.token = "Bearer " + responseBody.token!!
                        resetData()
                    } else {
                        _uiState.value = UiState.Error(response.message())
                        Log.e("ProfileViewModel", "onSuccess: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GetTokenResponse>, t: Throwable) {
                    _uiState.value = UiState.Error(t.message.toString())
                    Log.e("ProfileViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }

    fun resetData() {
        viewModelScope.launch {
            val client = ApiConfig.getRoutineApiService().deleteDailyRoutine(user.token)
            client.enqueue(object : Callback<DeleteDailyRoutineResponse> {
                override fun onResponse(
                    call: Call<DeleteDailyRoutineResponse>,
                    response: Response<DeleteDailyRoutineResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        _uiState.value = UiState.Success(user)
                    } else {
                        _uiState.value = UiState.Error(response.message())
                        Log.e("ProfileViewModel", "onSuccess: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DeleteDailyRoutineResponse>, t: Throwable) {
                    _uiState.value = UiState.Error(t.message.toString())
                    Log.e("ProfileViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }

    fun resetFormFilled() {
        viewModelScope.launch {
            repository.resetFormFilled()
        }
    }
}