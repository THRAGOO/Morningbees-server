package com.morningbees.util

import org.springframework.web.util.ContentCachingRequestWrapper
import java.util.*
import javax.servlet.http.HttpServletRequest

class RequestInfoLogging {
    fun makeLoggingRequestMap(req: HttpServletRequest): HashMap<String, Any> {
        val request = ContentCachingRequestWrapper(req)

        val requestInfos = HashMap<String, Any>()

        // request parameter
        val requestParameters = HashMap<String, Any?>()
        requestParameters["url"] = request.requestURL.toString()
        requestParameters["method"] = request.method
        requestParameters["req_path"] = request.requestURI
        requestParameters["encoding"] = request.characterEncoding
        requestParameters["remoteUser"] = request.remoteUser

        request.parameterMap.forEach { (k, v) ->
                requestParameters[k] = v.first()
        }
        if ( request.getAttribute("requestBody") != null ) {
            (request.getAttribute("requestBody") as HashMap<String, Any>).forEach { (k, v) ->
                requestParameters[k] = v
            }
        }

        // request header
        val requestHeaders = HashMap<String, Any>()
        val requestHeaderNameList = request.headerNames
        while (requestHeaderNameList.hasMoreElements()) {
            val headerName = requestHeaderNameList.nextElement()
            requestHeaders[headerName] = request.getHeader(headerName)
        }

        requestInfos["parameters"] = requestParameters
        requestInfos["headers"] = requestHeaders

        return requestInfos
    }
}
