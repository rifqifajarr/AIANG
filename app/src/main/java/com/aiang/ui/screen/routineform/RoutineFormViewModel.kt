package com.aiang.ui.screen.routineform

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.api.ApiConfig
import com.aiang.data.api.response.CreateActivitiesRequest
import com.aiang.data.api.response.CreateActivitiesResponse
import com.aiang.data.api.response.GetTokenResponse
import com.aiang.data.preferences.UserModel
import com.aiang.data.repository.Repository
import com.aiang.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoutineFormViewModel(private val repository: Repository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CreateActivitiesResponse>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<CreateActivitiesResponse>> get() = _uiState

    private lateinit var user: UserModel
    private var activityCreated = 0

    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collect { userData ->
                user = UserModel(
                    userId = userData.userId,
                    email = userData.email,
                    token = userData.token
                )
            }
        }
    }

    fun getToken() {
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
                        _uiState.value = UiState.Waiting
                        user.token = "Bearer " + responseBody.token!!
                        Log.i("getToken", user.token)
                    } else {
                        _uiState.value = UiState.Error(response.message())
                        Log.e("getToken", "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GetTokenResponse>, t: Throwable) {
                    _uiState.value = UiState.Error(t.message.toString())
                    Log.e("getToken", "onFailure: ${t.message}")
                }
            })
        }
    }

    fun createActivity(request: CreateActivitiesRequest) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val client = ApiConfig.getRoutineApiService().createDailyRoutine(user.token, request)
            client.enqueue(object : Callback<CreateActivitiesResponse> {
                override fun onResponse(
                    call: Call<CreateActivitiesResponse>,
                    response: Response<CreateActivitiesResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        Log.i("createActivity", responseBody.data?.day!!)
                        activityCreated++
                        if (activityCreated == 7) {
                            _uiState.value = UiState.Success(responseBody)
                        }
                    } else {
                        _uiState.value = UiState.Error(response.message())
                        Log.e("createActivity", "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CreateActivitiesResponse>, t: Throwable) {
                    _uiState.value = UiState.Error(t.message.toString())
                    Log.e("getToken", "onFailure: ${t.message}")
                }
            })
        }
    }

    fun markFormFilled() {
        viewModelScope.launch {
            if (!user.isFormFilled) {
                repository.markFormFilled()
            }
        }
    }
}