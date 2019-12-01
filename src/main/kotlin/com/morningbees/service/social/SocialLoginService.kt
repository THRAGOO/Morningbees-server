package com.morningbees.service.social

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.HttpURLConnection
import java.net.URL
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import java.net.URI

@Service
open class SocialLoginService() {

    open val socialLoginUrl = "https://openapi.naver.com/v1/nid/me"

    open fun getEmailByToken(socialToken: String): String { return "" }

    fun getResponseByUrl(socialToken: String): JsonNode {
        try {
            val parameters = LinkedMultiValueMap<String, String>()
            val headers = LinkedMultiValueMap<String, String>()

            headers.add("Authorization", "Bearer $socialToken")

            val request = HttpEntity(parameters, headers)

            val rest = RestTemplate()
            val result = rest.exchange(URI(socialLoginUrl), HttpMethod.GET, request, String::class.java)

            val objectMapper = ObjectMapper()
            val response = objectMapper.readTree(result.body)

            return response
        } catch (e: HttpClientErrorException) {
            throw BadRequestException("invalid social access token", ErrorCode.InvalidSocialToken, "EVENT_CODE")
        }
        catch (e: Exception) {
            throw BadRequestException(e.message!!, ErrorCode.BadRequest, "EVENT_CODE")
        }
    }

    fun isValidToken(socialToken: String): Boolean {
        try {
            val url = URL(socialLoginUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $socialToken")

            if (connection.responseCode != HttpStatus.OK.value()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }

        return true
    }
}