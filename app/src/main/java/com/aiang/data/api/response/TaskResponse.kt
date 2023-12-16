package com.aiang.data.api.response

import com.google.gson.annotations.SerializedName

data class CreateTaskResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: CreateTaskData? = null
)

data class CreateTaskData (
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,
)