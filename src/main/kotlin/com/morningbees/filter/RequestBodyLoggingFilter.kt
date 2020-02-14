package com.morningbees.filter

import com.morningbees.util.ReadableRequestBodyWrapper
import org.springframework.core.annotation.Order
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

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
            val wrapper: ReadableRequestBodyWrapper = ReadableRequestBodyWrapper(request as HttpServletRequest)
            wrapper.setAttribute("requestBody", wrapper.getRequestBody())

            chain?.doFilter(wrapper, response)
        } catch (e: Exception) {
            chain?.doFilter(request, response);
        }
    }
}