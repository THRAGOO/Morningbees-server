package com.morningbees.filter

import com.morningbees.exception.UnAuthorizeException
import com.morningbees.model.User
import com.morningbees.service.UserService
import com.morningbees.service.token.AccessTokenService
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

        val excludeUrls :Array<String> = arrayOf("/api/auth/sign_up", "/api/auth/sign_in", "/api/auth/renewal", "/api/auth/a", "/hello")
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
        } catch(e: UnAuthorizeException) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), e.message)
        } catch (e: Exception) {
            res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
        }
    }
}