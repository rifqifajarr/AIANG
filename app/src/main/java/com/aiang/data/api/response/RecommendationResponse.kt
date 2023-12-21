package com.aiang.data.api.response

import com.google.gson.annotations.SerializedName

data class Recommendation(
    var stressLevel: Int = 0,
    var recommendation: String = "",
    var recommendedTask: List<RecommendationTaskResponse> = emptyList()
)

data class ActivityRecommendationResponse(
    @field:SerializedName("Rekomendasi Aktivitas")
    val recommendation: String? = null,

    @field:SerializedName("Stress Level")
    val stressLevel: Int? = null
)

data class RecommendationTaskResponse(
    @field:SerializedName("Prediction Task")
    val predictionTask: String
)