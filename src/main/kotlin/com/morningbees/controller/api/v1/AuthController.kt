package com.morningbees.controller.api.v1

import com.morningbees.exception.BadRequestException
import com.morningbees.service.AuthService
import com.morningbees.service.social.NaverLoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
@RequestMapping("/api/auth")
class AuthController {
    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var naverLoginService: NaverLoginService

    @ResponseBody
    @PostMapping("/sign_up")
    fun signUp(@RequestParam(value = "socialAccessToken", required = true) socialAccessToken: String,
               @RequestParam(value = "provider", required = true) provider: String,
               @RequestParam(value = "nickname", required = true) nickname: String): ResponseEntity<HashMap<String, String>> {
        try {
            val email: String = naverLoginService.getEmailByToken(socialAccessToken)

            val response = authService.signUp(email, nickname, socialAccessToken, provider)

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch (e: Exception) {
            throw BadRequestException(e.message!!, 101, "EVENT_CODE")
        }
    }
}