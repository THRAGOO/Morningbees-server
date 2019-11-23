package com.morningbees.controller.api.v1

import com.morningbees.exception.InternalException
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @ResponseBody
    @PostMapping("/sign_up")
    fun signUp(@RequestParam(value = "socialAccessToken", required = true) socialAccessToken: String,
               @RequestParam(value = "provider", required = true) provider: String,
               @RequestParam(value = "nickname", required = true) nickname: String): ResponseEntity<HashMap<String, String>> {

        val response = HashMap<String, String>()
        response.put("accessToken", "accessToken")
        response.put("refreshToken", "refreshToken")

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(response)
    }
}