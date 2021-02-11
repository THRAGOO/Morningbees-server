package com.morningbees.dto

import javax.validation.constraints.NotNull


data class PenaltyPaidDto(
    @get:NotNull(message = "penalties can not null")
    val penalties: List<UserPenaltyDto> = mutableListOf()
)
