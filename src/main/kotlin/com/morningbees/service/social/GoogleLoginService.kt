package com.morningbees.service.social

import com.fasterxml.jackson.databind.ObjectMapper
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.util.LogEvent
import org.apache.commons.codec.binary.Base64
import org.slf4j.LoggerFactory

class GoogleLoginService : SocialLoginService() {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun getEmailByToken(socialToken: String): String {
        val notDecodePayload: String = socialToken.split(".")[1]

        val decodedPayload = String(Base64.decodeBase64(notDecodePayload), Charsets.UTF_8)

        val mapper = ObjectMapper()
        val payload = mapper.readTree(decodedPayload)

        if( payload["exp"].intValue() < System.currentTimeMillis()/1000 ) throw BadRequestException("google token expire", ErrorCode.InvalidAccessToken, LogEvent.SocialLoginServiceProcessError.code, logger)

        return payload ["email"].textValue()
    }
}
