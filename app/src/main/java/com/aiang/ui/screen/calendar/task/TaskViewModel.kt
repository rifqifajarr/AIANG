package com.aiang.ui.screen.calendar.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.api.ApiConfig
import com.aiang.data.api.response.GetTokenResponse
import com.aiang.data.api.response.Task
import com.aiang.data.preferences.UserModel
import com.aiang.data.repository.Repository
import com.aiang.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Task>>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<List<Task>>> get() = _uiState

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

    fun getTokenThenGetTask(date: LocalDate) {
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
                        getTask(date)
                    } else {
                        _uiState.value = UiState.Error(response.message())
                        Log.e("TaskViewModel", "onSuccess: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GetTokenResponse>, t: Throwable) {
                    _uiState.value = UiState.Error(t.message.toString())
                    Log.e("TaskViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }

    private fun getTask(localDate: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = localDate.format(formatter)

        val client = ApiConfig.getTaskApiService().getTasksByDate(user.token, date)
        client.enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _uiState.value = UiState.Success(responseBody)
                } else {
                    _uiState.value = UiState.Error("Phew, No Task Today")
                    Log.e("TaskViewModel", "getTask onSuccess: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                _uiState.value = UiState.Error(t.message.toString())
                Log.e("TaskViewModel", "getTask onFailure: ${t.message}")
            }
        })
    }
}