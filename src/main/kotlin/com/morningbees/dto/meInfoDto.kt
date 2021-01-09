package com.morningbees.dto

data class meInfoDto(
        val nickname: String = "",
        val alreadyJoin: Boolean? = null,
        val beeId: Long = 0,
        val userId: Long? = 0
)
