package com.morningbees.dto

import com.morningbees.model.Bee
import java.time.LocalTime
import javax.validation.constraints.*

object BeeValidConstants {
    const val LIMIT_START_TIME = 6
    const val LIMIT_END_TIME = 10
    const val MINIMUM_PAY = 2000
    const val MAXIMUM_PAY = 10000
}

data class BeeCreateDto(
        val title: String,
        val description: String,
        @get:DecimalMin(BeeValidConstants.LIMIT_START_TIME.toString(), message = "not match startTime")
        @get:DecimalMax(BeeValidConstants.LIMIT_END_TIME.toString(), message = "not match startTime")
        val startTime: Int,
        @get:DecimalMin(BeeValidConstants.LIMIT_START_TIME.toString(), message = "not match endTime")
        @get:DecimalMax(BeeValidConstants.LIMIT_END_TIME.toString(), message = "not match endTime")
        val endTime: Int,
        @get:DecimalMin(BeeValidConstants.MINIMUM_PAY.toString(), message = "not match pay")
        @get:DecimalMax(BeeValidConstants.MAXIMUM_PAY.toString(), message = "not match pay")
        val pay: Int) {
    fun toEntity(): Bee = Bee(title, description, LocalTime.of(startTime, 0, 0), LocalTime.of(endTime, 0, 0), pay)
}