package com.morningbees.filter

import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@WebFilter(urlPatterns= ["/api/*"])
@Order(1)
class CorsFilter : Filter {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        httpServletRequest.characterEncoding = "utf-8"
        //set header

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*")
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, access-control-allow-origin, authorization, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, X-BEES-ACCESS-TOKEN")
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, PATCH, OPTIONS")
        httpServletResponse.setHeader("Accept-Charset", "utf-8")
        httpServletResponse.setHeader("Cache-Control", "no-cache")
        httpServletResponse.setHeader("Expires", "-1")
        httpServletResponse.setHeader("Pragma", "no-cache")
        if ("OPTIONS" == httpServletRequest.method) {
            httpServletRequest.setAttribute("status", HttpServletResponse.SC_OK)
        } else {
            chain?.doFilter(httpServletRequest, httpServletResponse)
        }
    }
}
