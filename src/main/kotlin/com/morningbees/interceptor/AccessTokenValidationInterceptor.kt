package com.morningbees.interceptor

import com.morningbees.exception.ErrorCode
import com.morningbees.exception.UnAuthorizeException
import com.morningbees.model.User
import com.morningbees.service.UserService
import com.morningbees.service.token.AccessTokenService
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AccessTokenValidationInterceptor : HandlerInterceptorAdapter() {
    private val log = org.slf4j.LoggerFactory.getLogger(AccessTokenValidationInterceptor::class.java)

    @Autowired
    lateinit var accessTokenService: AccessTokenService

    @Autowired
    lateinit var userService: UserService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        try {
            val accessToken: String = request.getHeader("X-BEES-ACCESS-TOKEN")

            val tokenBody = accessTokenService.decodeAndGetInfos(accessToken)
            val user: User = userService.getUserById(tokenBody.userId)
            request.setAttribute("claims", tokenBody)
            request.setAttribute("user", user)

            return super.preHandle(request, response, handler)
        } catch(ex: UnAuthorizeException) {
            throw UnAuthorizeException(ex.message!!, ErrorCode.InvalidAccessToken, LogEvent.AccessTokenInterceptorProcess.code)
        } catch (ex: Exception) {
            throw UnAuthorizeException(ex.message!!, ErrorCode.InvalidAccessToken, LogEvent.AccessTokenInterceptorProcess.code)
        }
    }
}