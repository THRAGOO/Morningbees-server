package com.morningbees.service.social

import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class SocialLoginFactory {
    fun createFromProvider(provider: String) =
        when (provider) {
            "naver" -> NaverLoginService()
            "google" -> GoogleLoginService()
            else -> throw BadRequestException("unknown provider $provider", ErrorCode.UnknownProvider, "")
        }
}