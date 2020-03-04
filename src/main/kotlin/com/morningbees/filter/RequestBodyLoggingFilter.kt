package com.morningbees.filter

import com.morningbees.util.ReadableRequestBodyWrapper
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

@Component
@WebFilter(urlPatterns= ["/api/*"])
@Order(1)
class RequestBodyLoggingFilter : Filter {

    override fun init(filterConfig: FilterConfig?) {
        super.init(filterConfig)
    }

    override fun destroy() {
        super.destroy()
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        try {
            val req = request as HttpServletRequest
            if (req.getHeader("Content-Type").contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                chain?.doFilter(request, response)
            } else {
                val wrapper: ReadableRequestBodyWrapper = ReadableRequestBodyWrapper(req)
                wrapper.setAttribute("requestBody", wrapper.getRequestBody())

                chain?.doFilter(wrapper, response)
            }
        } catch (e: Exception) {
            chain?.doFilter(request, response);
        }
    }
}