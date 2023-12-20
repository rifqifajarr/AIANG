package com.aiang.ui.screen.calendar.routine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.api.ApiConfig
import com.aiang.data.api.response.GetActivitiesResponse
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

class RoutineViewModel(private val repository: Repository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<GetActivitiesResponse>>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<List<GetActivitiesResponse>>> get() = _uiState

    private lateinit var user: UserModel

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

    fun getTokenThenGetActivities() {
        val client = ApiConfig.getApiService().getToken(user.userId)
        client.enqueue(object : Callback<GetTokenResponse> {
            override fun onResponse(
                call: Call<GetTokenResponse>,
                response: Response<GetTokenResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    user.token = "Bearer " + responseBody.token!!
                    getActivities()
                } else {
                    _uiState.value = UiState.Error("Error: Can't Get Token")
                    Log.e("RoutineViewModel", "getTask onSuccess: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetTokenResponse>, t: Throwable) {
                _uiState.value = UiState.Error(t.message.toString())
                Log.e("RoutineViewModel", "getTask onSuccess: ${t.message}")
            }
        })
    }

    private fun getActivities() {
        val client = ApiConfig.getRoutineApiService().getDailyRoutine(user.token)
        client.enqueue(object : Callback<List<GetActivitiesResponse>> {
            override fun onResponse(
                call: Call<List<GetActivitiesResponse>>,
                response: Response<List<GetActivitiesResponse>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _uiState.value = UiState.Success(responseBody)
                } else {
                    _uiState.value = UiState.Error("Error: Can't Get Routine")
                    Log.e("RoutineViewModel", "getTask onSuccess: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GetActivitiesResponse>>, t: Throwable) {
                _uiState.value = UiState.Error(t.message.toString())
                Log.e("RoutineViewModel", "getTask onFailure: ${t.message}")
            }
        })
    }
}