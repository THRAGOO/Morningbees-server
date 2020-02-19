package com.morningbees.controller.api.v1

import com.morningbees.dto.SignInDto
import com.morningbees.dto.SignUpDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.exception.MorningbeesException
import com.morningbees.model.User
import com.morningbees.service.AuthService
import com.morningbees.service.UserService
import com.morningbees.service.UserTokenService
import com.morningbees.service.social.SocialLoginFactory
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import org.slf4j.LoggerFactory
import org.springframework.validation.Errors
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private val SIGN_UP_TYPE = 0
    private val SIGN_IN_TYPE = 1
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userTokenService: UserTokenService

    @Autowired
    lateinit var socialLoginFactory: SocialLoginFactory

    @ResponseBody
    @PostMapping("/sign_up")
    fun signUp(@RequestBody @Valid signUpDto: SignUpDto, errors: Errors): ResponseEntity<HashMap<String, Any>> {
        val socialAccessToken: String = signUpDto.socialAccessToken
        val provider: String = signUpDto.provider
        val nickname: String = signUpDto.nickname

        try {
            val email: String =  socialLoginFactory.createFromProvider(provider).getEmailByToken(socialAccessToken)

            val user: User = userService.signUpWithProvider(email, nickname, provider)

            val response: HashMap<String, Any> = authService.getAuthTokens(user)

            userTokenService.saveUserToken(user, null, response["refreshToken"].toString())

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch (e: MorningbeesException) {
            throw BadRequestException(e.message!!, e.code, e.logEventCode)
        } catch (e: Exception) {
            throw BadRequestException(e.message!!, ErrorCode.BadRequest, LogEvent.SignUpError.code)
        }
    }

    @ResponseBody
    @PostMapping("/sign_in")
    fun signIn(@RequestBody @Valid signInDto: SignInDto): ResponseEntity<HashMap<String, Any>> {
        try {
            val socialAccessToken: String = signInDto.socialAccessToken
            val provider: String = signInDto.provider

            val email: String =  socialLoginFactory.createFromProvider(provider).getEmailByToken(socialAccessToken)

            val user: User? = userService.getUserByEmail(email)

            var response: HashMap<String, Any> = HashMap()

            if ( user == null ) {
                response.put("accessToken", "")
                response.put("refreshToken", "")
                response.put("type", SIGN_UP_TYPE)
            } else {
                response = authService.getAuthTokens(user)
                response.put("type", SIGN_IN_TYPE)

                userTokenService.saveUserToken(user, null, response["refreshToken"].toString())
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch (e: MorningbeesException) {
            throw BadRequestException(e.message!!, e.code, e.logEventCode)
        } catch (e: Exception) {
            throw BadRequestException(e.message!!, ErrorCode.BadRequest, LogEvent.SignInError.code)
        }
    }

    @ResponseBody
    @GetMapping("/valid_nickname")
    fun isValidNickname(@RequestParam(value = "nickname", required = true) nickname: String): ResponseEntity<HashMap<String, Any>> {
        try {
            val result = userService.isExistsNickname(nickname)

            val response: HashMap<String, Any> = HashMap()
            response.put("isValid", result)

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch (e: MorningbeesException) {
            throw BadRequestException(e.message!!, e.code, e.logEventCode)
        } catch (e: Exception) {
            throw BadRequestException(e.message!!, ErrorCode.BadRequest, LogEvent.ValidNicknameError.code)
        }
    }

    @ResponseBody
    @PostMapping("/renewal")
    fun renewal(request: HttpServletRequest): ResponseEntity<HashMap<String, Any>> {
        try {
            val user: User = request.getAttribute("user") as User
            val tokenInfos= authService.getAuthTokens(user)
            val response: HashMap<String, Any> = HashMap()
            response.put("accessToken", tokenInfos["accessToken"].toString())

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch (e: MorningbeesException) {
            throw BadRequestException(e.message!!, e.code, e.logEventCode)
        } catch (e: Exception) {
            throw BadRequestException(e.message!!, ErrorCode.BadRequest, LogEvent.RenewalError.code)
        }
    }

    @ResponseBody
    @GetMapping("/me")
    fun me(request: HttpServletRequest): ResponseEntity<HashMap<String, Any>> {
        try {
            val user: User = request.getAttribute("user") as User

            val response = userService.me(user)

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch (e: MorningbeesException) {
            throw BadRequestException(e.message!!, e.code, e.logEventCode)
        } catch (e: Exception) {
            throw BadRequestException(e.message!!, ErrorCode.BadRequest, LogEvent.MeError.code)
        }
    }
}