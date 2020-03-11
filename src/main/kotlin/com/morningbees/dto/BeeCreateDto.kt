package com.morningbees.dto

import com.morningbees.model.Bee
import java.time.LocalTime

data class BeeCreateDto(val title: String, val description: String, val startTime: Int, val endTime: Int, val pay: Int) {
    fun toEntity(): Bee = Bee(title, description, LocalTime.of(startTime, 0, 0), LocalTime.of(endTime, 0, 0), pay)
}