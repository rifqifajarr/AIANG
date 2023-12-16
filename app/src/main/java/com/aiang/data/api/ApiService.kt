package com.aiang.data.api

import com.aiang.data.api.response.CreateTaskResponse
import com.aiang.data.api.response.GetTokenResponse
import com.aiang.data.api.response.LoginResponse
import com.aiang.data.api.response.RegisterResponse
import retrofit2.Call
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
    @FormUrlEncoded
    @POST("tasks/")
    fun createTask(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("desc") desc: String,
        @Field("category") category: String,
        @Field("priority") priority: String,
        @Field("date") date: String,
    ): Call<CreateTaskResponse>
}