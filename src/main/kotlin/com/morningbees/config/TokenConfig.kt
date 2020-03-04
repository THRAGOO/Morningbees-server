package com.morningbees.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class TokenConfig (
        @Value("\${token.salt}")
        var salt: String = "secret"
)