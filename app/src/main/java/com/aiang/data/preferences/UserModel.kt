package com.aiang.data.preferences

data class UserModel(
    var name: String = "",
    var userId: String,
    var email: String,
    var token: String = "",
    val isLogin: Boolean = false,
    val isFormFilled: Boolean = false
)
