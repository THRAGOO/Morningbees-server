package com.morningbees.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class MissionInfoDto (
        @JsonProperty("missionId") val missionId: Long,
        @JsonProperty("imageUrl") val imageUrl: String,
        @JsonProperty("nickname") val nickname: String,
        @JsonProperty("agreeCount") val agreeCount: BigDecimal,
        @JsonProperty("disagreeCount") val disagreeCount: BigDecimal) {

}