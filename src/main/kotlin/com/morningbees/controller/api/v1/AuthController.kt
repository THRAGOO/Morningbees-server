package com.morningbees.controller.api.v1

import com.morningbees.exception.BadRequestException
import com.morningbees.model.User
import com.morningbees.service.AuthService
import com.morningbees.service.UserService
import com.morningbees.service.social.SocialLoginFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private val SIGN_UP_TYPE = 0
    private val SIGN_IN_TYPE = 1

    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var socialLoginFactory: SocialLoginFactory

    @ResponseBody
    @PostMapping("/sign_up")
    fun signUp(@RequestParam(value = "socialAccessToken", required = true) socialAccessToken: String,
               @RequestParam(value = "provider", required = true) provider: String,
               @RequestParam(value = "nickname", required = true) nickname: String): ResponseEntity<HashMap<String, Any>> {
        try {
            val email: String =  socialLoginFactory.createFromProvider(provider).getEmailByToken(socialAccessToken)

            val user: User = userService.signUpWithProvider(email, nickname, provider)

            val response: HashMap<String, Any> = authService.getAuthTokens(user)

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch (e: Exception) {
            throw BadRequestException(e.message!!, 101, "EVENT_CODE")
        }
    }

    @ResponseBody
    @PostMapping("/sign_in")
    fun signIp(@RequestParam(value = "socialAccessToken", required = true) socialAccessToken: String,
               @RequestParam(value = "provider", required = true) provider: String): ResponseEntity<HashMap<String, Any>> {
        try {
            val email: String =  socialLoginFactory.createFromProvider(provider).getEmailByToken(socialAccessToken)

            val user: User? = userService.getUserByEmail(email)
            
            var response: HashMap<String, Any> = HashMap<String, Any>()

            if ( user == null ) {
                response.put("accessToken", "")
                response.put("refreshToken", "")
                response.put("type", SIGN_UP_TYPE)
            } else {
                response = authService.getAuthTokens(user)
                response.put("type", SIGN_IN_TYPE)
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch (e: Exception) {
            throw BadRequestException(e.message!!, 101, "EVENT_CODE")
        }
    }
}