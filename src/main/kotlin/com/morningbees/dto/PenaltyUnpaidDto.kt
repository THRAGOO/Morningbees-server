package com.morningbees.dto

import javax.validation.constraints.NotNull


data class PenaltyUnpaidDto(
    @get:NotNull(message = "user id can not null")
    val userId: Long,
    @get:NotNull(message = "penalty can not null")
    val penalty: Int
)
