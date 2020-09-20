package com.morningbees.service.social

import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.UserProvider
import com.morningbees.util.LogEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SocialLoginFactory {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    fun createFromProvider(provider: String) =
        when (provider) {
            UserProvider.Provider.Naver.provider-> NaverLoginService()
            UserProvider.Provider.Google.provider -> GoogleLoginService()
            UserProvider.Provider.Apple.provider -> AppleLoginService()
            else -> throw BadRequestException("unknown provider $provider", ErrorCode.UnknownProvider, LogEvent.SocialLoginFactoryProcessError.code, logger)
        }
}