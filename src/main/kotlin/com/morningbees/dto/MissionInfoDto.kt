package com.morningbees.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime

data class MissionInfoDto (
        @JsonProperty("missionId") val missionId: Long,
        @JsonProperty("imageUrl") val imageUrl: String,
        @JsonProperty("nickname") val nickname: String,
        @JsonProperty("type") val type: Int,
        @JsonProperty("difficulty") val difficulty: Int,
        @JsonProperty("createdAt") val createdAt: String,
        @JsonProperty("agreeCount") val agreeCount: BigDecimal,
        @JsonProperty("disagreeCount") val disagreeCount: BigDecimal) {

}