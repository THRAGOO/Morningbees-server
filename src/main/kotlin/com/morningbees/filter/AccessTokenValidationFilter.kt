package com.morningbees.filter

import com.morningbees.controller.ErrorController
import com.morningbees.exception.UnAuthorizeException
import com.morningbees.model.User
import com.morningbees.service.UserService
import com.morningbees.service.token.AccessTokenService
import com.morningbees.util.LogEvent
import net.logstash.logback.argument.StructuredArguments
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//@Component
@WebFilter(urlPatterns= ["/api/*"])
@Order(2)
class AccessTokenValidationFilter : Filter {
    private val log = org.slf4j.LoggerFactory.getLogger(AccessTokenValidationFilter::class.java)

    @Autowired
    lateinit var accessTokenService: AccessTokenService

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

        val excludeUrls :Array<String> = arrayOf("/api/auth/sign_up", "/api/auth/sign_in", "/api/auth/renewal", "/api/auth/valid_nickname", "/hello")
        val path = request.requestURI
        if (excludeUrls.contains(path)) {
            chain?.doFilter(request, response)
            return
        }

        try {
            val accessToken: String = req.getHeader("X-BEES-ACCESS-TOKEN")

            val tokenBody = accessTokenService.decodeAndGetInfos(accessToken)
            val user: User = userService.getUserById(tokenBody.userId)
            req.setAttribute("claims", tokenBody)
            req.setAttribute("user", user)

            chain?.doFilter(request, response)
        } catch(ex: UnAuthorizeException) {
            log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.GlobalException), StructuredArguments.kv("backTrace", ex.stackTrace[0].toString()))
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "잘 못된 요청입니다.")
        } catch (ex: Exception) {
            log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.GlobalException), StructuredArguments.kv("backTrace", ex.stackTrace[0].toString()))
            res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "잘 못된 요청입니다.")
        }
    }
}