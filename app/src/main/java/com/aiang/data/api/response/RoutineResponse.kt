package com.aiang.data.api.response

import com.google.gson.annotations.SerializedName

data class CreateActivitiesRequest(
    var day: String = "",
    var workcoll_start: String = "",
    var workcoll_end: String = "",
    var break_start: String = "",
    var break_end: String = "",
    var studyhome_start: String = "",
    var studyhome_end: String = "",
    var sleep_start: String = "",
    var sleep_end: String = ""
)

data class CreateActivitiesResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: CreateActivitiesData? = null
)

data class CreateActivitiesData (
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("day")
    val day: String? = null,
)

data class GetActivitiesResponse(
    val id: String,
    val day: String,
    val workcoll_start: String,
    val workcoll_end: String,
    val break_start: String,
    val break_end: String,
    val studyhome_start: String,
    val studyhome_end: String,
    val sleep_start: String,
    val sleep_end: String,
    val user_id: String,
    val createdAt: String,
    val updatedAt: String
)

data class DeleteDailyRoutineResponse(
    val error: Boolean,
    val message: String
)