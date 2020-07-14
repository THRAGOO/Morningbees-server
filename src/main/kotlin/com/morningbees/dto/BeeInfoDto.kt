package com.morningbees.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalTime

data class BeeInfoDto(
        @JsonProperty("isManager")val isManager:Boolean,
        @JsonProperty ("title") val title:String,
        @JsonProperty("startTime")val startTime:String,
        @JsonProperty("totalPay") val totalPay:Int,
        @JsonProperty("todayUser") val todayUser:String,
        @JsonProperty("birthDay")val birthDay:String ) {

}