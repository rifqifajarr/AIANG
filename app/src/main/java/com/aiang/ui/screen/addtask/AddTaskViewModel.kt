package com.aiang.ui.screen.addtask

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aiang.data.api.ApiConfig
import com.aiang.data.api.response.CreateTaskResponse
import com.aiang.data.api.response.GetTokenResponse
import com.aiang.data.preferences.UserModel
import com.aiang.data.repository.Repository
import com.aiang.ui.common.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTaskViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CreateTaskResponse>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<CreateTaskResponse>> get() = _uiState

//    private var _token = ""
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

    fun getTokenThenAddTask(name: String, desc: String, category: String, priority: String, date: String) {
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
                        Log.i("getToken", user.token)
                        createTask(name, desc, category, priority, date)
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

    fun createTask(name: String, desc: String, category: String, priority: String, date: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val client = ApiConfig.getTaskApiService().createTask(user.token, name, desc, category, priority, date)
            client.enqueue(object  : Callback<CreateTaskResponse> {
                override fun onResponse(
                    call: Call<CreateTaskResponse>,
                    response: Response<CreateTaskResponse>
                ) {
                    _uiState.value = UiState.Waiting
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        _uiState.value = UiState.Success(responseBody)
                    } else {
                        _uiState.value = UiState.Error(response.message())
                        Log.e("AddTaskViewModel", "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CreateTaskResponse>, t: Throwable) {
                    _uiState.value = UiState.Error(t.message.toString())
                    Log.e("AddTaskViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }
}