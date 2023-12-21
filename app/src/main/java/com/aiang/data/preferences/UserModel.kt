package com.aiang.data.preferences

import com.aiang.data.api.response.Task

data class UserModel(
    var name: String = "",
    var userId: String,
    var email: String,
    var token: String = "",
    val isLogin: Boolean = false,
    val isFormFilled: Boolean = false,
    val finishedTaskId: List<String> = emptyList()
)
