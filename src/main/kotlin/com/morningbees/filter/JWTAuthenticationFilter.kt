package com.morningbees.filter

import com.morningbees.exception.UnAuthorizeException
import com.morningbees.model.User
import com.morningbees.service.token.AccessTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns= ["/api/v1/*"])
class JWTAuthenticationFilter : Filter {
    @Autowired
    lateinit var accessTokenService: AccessTokenService

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
            val accessToken: String = req.getHeader("X-BEES-ACCESS-TOKEN")

            val tokenBody = accessTokenService.decodeAndGetInfos(accessToken)
            req.setAttribute("claims", tokenBody)
            req.setAttribute("User", User(tokenBody.nickname))

            chain?.doFilter(request, response);
        } catch(e: UnAuthorizeException) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), e.message)
        } catch (e: Exception) {
            res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
        }
    }
}