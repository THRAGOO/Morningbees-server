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
@Order(1)
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
            logger.info("Test11", StructuredArguments.kv("eventCode", LogEvent.BeeMemberServiceProcess.code))

            val req = request as HttpServletRequest
            if (req.getHeader("Content-Type").contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                logger.info("Test22", StructuredArguments.kv("eventCode", LogEvent.BeeMemberServiceProcess.code))
                chain?.doFilter(request, response)
            } else {
                logger.info("Test33", StructuredArguments.kv("eventCode", LogEvent.BeeMemberServiceProcess.code))
                val wrapper: ReadableRequestBodyWrapper = ReadableRequestBodyWrapper(req)
                wrapper.setAttribute("requestBody", wrapper.getRequestBody())

                chain?.doFilter(wrapper, response)
            }
        } catch (e: Exception) {
            logger.warn(e.message, StructuredArguments.kv("eventCode", "EVENT_CODE"), StructuredArguments.kv("backTrace", e.stackTrace[0].toString() + e.stackTrace[1].toString()))
            chain?.doFilter(request, response);
        }
    }
}