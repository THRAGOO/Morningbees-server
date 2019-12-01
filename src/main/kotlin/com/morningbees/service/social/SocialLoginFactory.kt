package com.morningbees.service.social

import org.springframework.stereotype.Component

@Component
class SocialLoginFactory {
    fun createFromProvider(provider: String) =
        when (provider) {
            "naver" -> NaverLoginService()
            "google" -> GoogleLoginService()
            else -> throw Exception("Don't know provider $provider")
        }
}