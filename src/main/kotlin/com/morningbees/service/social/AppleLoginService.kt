package com.morningbees.service.social

import com.fasterxml.jackson.databind.ObjectMapper
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.util.LogEvent
import org.apache.commons.codec.binary.Base64

class AppleLoginService : SocialLoginService() {

    override fun getEmailByToken(socialToken: String): String {
        val notDecodePayload: String = socialToken.split(".")[1]

        val decodedPayload = String(Base64.decodeBase64(notDecodePayload), Charsets.UTF_8)

        val mapper = ObjectMapper()
        val payload = mapper.readTree(decodedPayload)

//        if( payload["is_private_email"] != null ) throw BadRequestException("pass apple private email", ErrorCode.ApplePrivateEmail, LogEvent.SocialLoginServiceProcessError.code)
        if( payload["exp"].intValue() < System.currentTimeMillis()/1000 ) throw BadRequestException("apple token expire", ErrorCode.InvalidAccessToken, LogEvent.SocialLoginServiceProcessError.code)

        return payload ["email"].textValue()
    }
}
