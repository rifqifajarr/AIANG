package com.aiang.data.preferences

data class UserModel(
    var name: String = "",
    var userId: String,
    var email: String,
    val token: String = "",
    val isLogin: Boolean = false
)
