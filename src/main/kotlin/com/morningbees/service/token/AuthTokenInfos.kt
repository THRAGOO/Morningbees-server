package com.morningbees.service.token

data class AuthTokenInfos (
        var userId: Long,
        var nickname: String,
        var provider: String,
        var tokenType: Int
) {
    constructor() : this(0, "", "", 0)
}