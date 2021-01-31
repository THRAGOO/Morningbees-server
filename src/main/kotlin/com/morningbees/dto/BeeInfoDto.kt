package com.morningbees.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalTime

data class BeeInfoDto(
        @JsonProperty("isManager")val isManager: Boolean,
        @JsonProperty("nickname")val nickname: String,
        @JsonProperty("startTime")val startTime: LocalTime,
        @JsonProperty("endTime")val endTime: LocalTime,
        @JsonProperty("pay") val pay: Int
)
