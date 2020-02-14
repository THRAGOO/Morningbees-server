package com.morningbees.dto

data class SignUpDto(val socialAccessToken: String, val provider: String, val nickname: String)