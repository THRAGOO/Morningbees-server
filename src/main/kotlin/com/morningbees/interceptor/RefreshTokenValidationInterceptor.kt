package com.morningbees.interceptor

import com.morningbees.exception.ErrorCode
import com.morningbees.exception.UnAuthorizeException
import com.morningbees.model.User
import com.morningbees.service.UserService
import com.morningbees.service.token.RefreshTokenService
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RefreshTokenValidationInterceptor : HandlerInterceptorAdapter() {
    private val logger = org.slf4j.LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var refreshTokenService: RefreshTokenService

    @Autowired
    lateinit var userService: UserService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        try {
            val refreshToken: String = request.getHeader("X-BEES-REFRESH-TOKEN")

            val tokenBody = refreshTokenService.decodeAndGetInfos(refreshToken)
            val user: User = userService.findById(tokenBody.userId)
            request.setAttribute("claims", tokenBody)
            request.setAttribute("user", user)

            if (user.token!!.refreshToken != refreshToken) {
                throw UnAuthorizeException("not valid refresh token", ErrorCode.InvalidAccessToken, LogEvent.TokenServiceProcessError.code)
            }

            return super.preHandle(request, response, handler)
        } catch(ex: UnAuthorizeException) {
            throw UnAuthorizeException(ex.message!!, ErrorCode.InvalidAccessToken, LogEvent.AccessTokenInterceptorProcess.code, logger)
        } catch (ex: Exception) {
            throw UnAuthorizeException(ex.message!!, ErrorCode.InvalidAccessToken, LogEvent.AccessTokenInterceptorProcess.code, logger)
        }
    }
}