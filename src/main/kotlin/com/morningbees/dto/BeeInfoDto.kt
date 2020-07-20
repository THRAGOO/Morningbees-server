package com.morningbees.dto

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.morningbees.model.User
import java.time.LocalTime

data class BeeInfoDto(
        @JsonProperty("isManager")val isManager:Boolean,
        @JsonProperty("nickname")val nickname:String,
        @JsonProperty("users")val users: User,
        @JsonProperty("startTime")val startTime:String,
        @JsonProperty("endTime")val endTime: String,
        @JsonProperty("totalPay") val totalPay:Int) {

}