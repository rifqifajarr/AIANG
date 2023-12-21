package com.aiang.data.api.response

import com.google.gson.annotations.SerializedName

data class Recommendation(
    var stressLevel: Int = 0,
    var recommendation: String = "",
    var recommendedTask: List<String> = emptyList()
)

data class ActivityRecommendationResponse(
    @field:SerializedName("Rekomendasi Aktivitas")
    val recommendation: String? = null,

    @field:SerializedName("Stress Level")
    val stressLevel: Int? = null
)
