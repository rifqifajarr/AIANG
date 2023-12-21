package com.aiang.data.api

import com.aiang.data.api.response.ActivityRecommendationResponse
import com.aiang.data.api.response.CreateActivitiesRequest
import com.aiang.data.api.response.CreateActivitiesResponse
import com.aiang.data.api.response.CreateTaskResponse
import com.aiang.data.api.response.DeleteDailyRoutineResponse
import com.aiang.data.api.response.GetActivitiesResponse
import com.aiang.data.api.response.GetTokenResponse
import com.aiang.data.api.response.LoginResponse
import com.aiang.data.api.response.RecommendationTaskResponse
import com.aiang.data.api.response.RegisterResponse
import com.aiang.data.api.response.Task
import com.aiang.data.api.response.TaskRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("users")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @DELETE("logout")
    fun logout(): Call<LoginResponse>

    @GET("token/{id}")
    fun getToken(@Path("id") id: String): Call<GetTokenResponse>
}

interface TaskApiService {
    @POST("tasks/")
    fun createTask(
        @Header("Authorization") token: String,
        @Body taskRequest: TaskRequest
    ): Call<CreateTaskResponse>

    @GET("tasks/date/{date}")
    fun getTasksByDate(
        @Header("Authorization") token: String,
        @Path("date") date: String
    ): Call<List<Task>>

    @GET("/prediction/tasks")
    fun getTaskRecommendation(
        @Header("Authorization") token: String,
    ): Call<List<RecommendationTaskResponse>>
}

interface RoutineApiService {
    @POST("/activities/")
    fun createDailyRoutine(
        @Header("Authorization") token: String,
        @Body routineRequest: CreateActivitiesRequest
    ): Call<CreateActivitiesResponse>

    @GET("/activities")
    fun getDailyRoutine(
        @Header("Authorization") token: String,
    ): Call<List<GetActivitiesResponse>>

    @DELETE("/activities")
    fun deleteDailyRoutine(
        @Header("Authorization") token: String,
    ): Call<DeleteDailyRoutineResponse>

    @GET("/prediction/activities/{activityId}")
    fun getActivityRecommendation(
        @Header("Authorization") token: String,
        @Path("activityId") taskId: String
    ): Call<ActivityRecommendationResponse>
}