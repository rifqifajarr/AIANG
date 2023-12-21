package com.aiang.ui.screen.recommendation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiang.data.api.ApiConfig
import com.aiang.data.api.response.ActivityRecommendationResponse
import com.aiang.data.api.response.GetTokenResponse
import com.aiang.data.api.response.Recommendation
import com.aiang.data.api.response.RecommendationTaskResponse
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

class RecommendationViewModel(private val repository: Repository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Recommendation>> = MutableStateFlow(UiState.Waiting)
    val uiState: StateFlow<UiState<Recommendation>> get() = _uiState

    private lateinit var user: UserModel
    private var recommendation = Recommendation()

    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collect { userData ->
                user = UserModel(
                    userId = userData.userId,
                    email = userData.email,
                    token = userData.token,
                )
            }
        }
    }

    fun getTokenThenGetData(taskId: String) {
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
                        getRoutineRecommendation(taskId)
                        getTaskRecommendation()
                    } else {
                        _uiState.value = UiState.Error(response.message())
                        Log.e("GetToken", "onSuccess: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GetTokenResponse>, t: Throwable) {
                    _uiState.value = UiState.Error(t.message.toString())
                    Log.e("GetToken", "onFailure: ${t.message}")
                }
            })
        }
    }

    private fun getRoutineRecommendation(taskId: String) {
        val client = ApiConfig.getRoutineApiService().getActivityRecommendation(user.token, taskId)
        client.enqueue(object : Callback<ActivityRecommendationResponse> {
            override fun onResponse(
                call: Call<ActivityRecommendationResponse>,
                response: Response<ActivityRecommendationResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    recommendation.recommendation = responseBody.recommendation.toString()
                    recommendation.stressLevel = responseBody.stressLevel!!
                } else {
                    _uiState.value = UiState.Error(response.message())
                    Log.e("Get Routine Recommendation", "onSuccess: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ActivityRecommendationResponse>, t: Throwable) {
                Log.e("Get Routine Recommendation", "onFailure: ${t.message}")
            }
        })
    }

    private fun getTaskRecommendation() {
        val client = ApiConfig.getTaskApiService().getTaskRecommendation(user.token)
        client.enqueue(object : Callback<List<RecommendationTaskResponse>> {
            override fun onResponse(call: Call<List<RecommendationTaskResponse>>, response: Response<List<RecommendationTaskResponse>>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    recommendation.recommendedTask = responseBody
                    Log.i("RecommendationList", recommendation.recommendedTask.toString())
//                    Log.i("ResponseBody", responseBody.predictionTask)

                    _uiState.value = UiState.Success(recommendation)
                } else {
                    _uiState.value = UiState.Error(response.message())
                    Log.e("Get Task Recommendation", "onSuccess: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<RecommendationTaskResponse>>, t: Throwable) {
                Log.e("Get Task Recommendation", "onFailure: ${t.message}")
            }
        })
    }
}