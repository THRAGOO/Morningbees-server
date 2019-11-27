package com.morningbees.service.social

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.morningbees.exception.BadRequestException
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import org.springframework.http.HttpStatus

@Service
class NaverLoginService {

    fun getEmailByToken(socialToken: String): String {
        var email: String = ""

        try {
            val apiURL = "https://openapi.naver.com/v1/nid/me"

            val url = URL(apiURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $socialToken")

            if (connection.responseCode != HttpStatus.OK.value()) {
                throw BadRequestException("naver login unauthorized", 201, "EVENT_CODE")
            }

            val br: BufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
            val objectMapper = ObjectMapper()
            val response: JsonNode = objectMapper.readTree(br.readLine())

            email = response["response"]["email"].toString()

            br.close()
        } catch (e: Exception) {
            throw BadRequestException(e.message!!, 202, "EVENT_CODE")
        }

        return email
    }

    fun isValidToken(socialToken: String): Boolean {
        try {
            val apiURL = "https://openapi.naver.com/v1/nid/me"

            val url = URL(apiURL)
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
