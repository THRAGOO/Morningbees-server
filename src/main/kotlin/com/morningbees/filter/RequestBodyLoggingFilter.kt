package com.morningbees.filter

import com.morningbees.util.ReadableRequestBodyWrapper
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

@Component
@WebFilter(urlPatterns= ["/api/*"])
@Order(2)
class RequestBodyLoggingFilter : Filter {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        try {
            val req = request as HttpServletRequest

            val contentType: String = req.getHeader("Content-Type") ?: return chain?.doFilter(request, response)!!
            if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                chain?.doFilter(request, response)
            } else {
                val wrapper = ReadableRequestBodyWrapper(req)
                wrapper.setAttribute("requestBody", wrapper.getRequestBody())

                chain?.doFilter(wrapper, response)
            }
        } catch (e: Exception) {
            logger.warn(e.message + " " + e.stackTrace)
            chain?.doFilter(request, response)
        }
    }
}