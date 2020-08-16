package com.morningbees.service.social

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.util.LogEvent
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import java.net.URI

@Service
class SocialLoginService {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    val socialLoginUrl = "https://openapi.naver.com/v1/nid/me"

    fun getEmailByToken(socialToken: String): String { return "" }

    fun getResponseByUrl(socialToken: String): JsonNode {
        try {
            val parameters = LinkedMultiValueMap<String, String>()
            val headers = LinkedMultiValueMap<String, String>()

            headers.add("Authorization", "Bearer $socialToken")

            val request = HttpEntity(parameters, headers)

            val rest = RestTemplate()
            val result = rest.exchange(URI(socialLoginUrl), HttpMethod.GET, request, String::class.java)

            val objectMapper = ObjectMapper()

            return objectMapper.readTree(result.body)
        } catch (e: HttpClientErrorException) {
            throw BadRequestException("invalid social access token", ErrorCode.InvalidSocialToken, LogEvent.SocialLoginServiceProcessError.code, logger)
        }
        catch (e: Exception) {
            throw BadRequestException(e.message!!, ErrorCode.BadRequest, LogEvent.SocialLoginServiceProcessError.code, logger)
        }
    }
}