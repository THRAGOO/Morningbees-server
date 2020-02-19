package com.morningbees.filter

import com.morningbees.exception.ErrorCode
import com.morningbees.exception.UnAuthorizeException
import com.morningbees.model.User
import com.morningbees.service.UserService
import com.morningbees.service.token.RefreshTokenService
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//@Component
@WebFilter(urlPatterns= ["/api/auth/renewal"])
@Order(3)
class RefreshTokenValidationFilter : Filter {
    @Autowired
    lateinit var refreshTokenService: RefreshTokenService

    @Autowired
    lateinit var userService: UserService

    override fun init(filterConfig: FilterConfig?) {
        super.init(filterConfig)
    }

    override fun destroy() {
        super.destroy()
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val req:HttpServletRequest = request as HttpServletRequest
        val res:HttpServletResponse = response as HttpServletResponse

        try {
            val refreshToken: String = req.getHeader("X-BEES-REFRESH-TOKEN")

            val tokenBody = refreshTokenService.decodeAndGetInfos(refreshToken)
            val user: User = userService.getUserById(tokenBody.userId)
            req.setAttribute("claims", tokenBody)
            req.setAttribute("user", user)

            if (user.token!!.refreshToken != refreshToken) {
                throw UnAuthorizeException("not valid refresh token", ErrorCode.InvalidAccessToken, LogEvent.TokenServiceProcessError.code)
            }

            chain?.doFilter(request, response)
        } catch(e: UnAuthorizeException) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), e.message)
        } catch (e: Exception) {
            res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
        }
    }
}