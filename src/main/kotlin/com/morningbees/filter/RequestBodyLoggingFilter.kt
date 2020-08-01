package com.morningbees.filter

import com.morningbees.util.LogEvent
import com.morningbees.util.ReadableRequestBodyWrapper
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

@Component
@WebFilter(urlPatterns= ["/api/*"])
@Order(2)
class RequestBodyLoggingFilter : Filter {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun init(filterConfig: FilterConfig?) {
        super.init(filterConfig)
    }

    override fun destroy() {
        super.destroy()
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        try {
            val req = request as HttpServletRequest

            val contentType: String? = req.getHeader("Content-Type")
            if (contentType == null) {
                return chain?.doFilter(request, response)!!
            }
            val accessToken = request.getHeader("X-BEES-ACCESS-TOKEN")
            logger.info("Token: $accessToken")

            if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                logger.info("Test22")
                chain?.doFilter(request, response)
            } else {
                logger.info("Test33")
                val wrapper: ReadableRequestBodyWrapper = ReadableRequestBodyWrapper(req)
                wrapper.setAttribute("requestBody", wrapper.getRequestBody())

                chain?.doFilter(wrapper, response)
            }
        } catch (e: Exception) {
            logger.warn(e.message + " " + e.stackTrace)
            chain?.doFilter(request, response);
        }
    }
}