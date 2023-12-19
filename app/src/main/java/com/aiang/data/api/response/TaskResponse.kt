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

data class Task (
    val id: String,
    val name: String,
    val desc: String,
    val category: String,
    val priority: String,
    val date: String,
    val user_id: String,
    val createdAt: String,
    val updatedAt: String
)

data class TaskRequest(
    val name: String,
    val desc: String,
    val category: String,
    val priority: String,
    val date: String
)