package com.aiang.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,
)

data class LoginResult(
    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)

data class GetTokenResponse (
    @field:SerializedName("accessToken")
    val token: String? = null
)