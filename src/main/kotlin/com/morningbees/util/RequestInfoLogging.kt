package com.morningbees.util

import java.util.HashMap
import javax.servlet.http.HttpServletRequest

class RequestInfoLogging {
    public fun makeLoggingRequestMap(request: HttpServletRequest): HashMap<String, Any> {
        val requestInfos = HashMap<String, Any>()

        // request parameter
        val requestParameters = HashMap<String, Any?>()
        requestParameters["url"] = request.requestURL.toString()
        requestParameters["method"] = request.method
        requestParameters["req_path"] = request.requestURI
        requestParameters["encoding"] = request.characterEncoding
        requestParameters["remoteUser"] = request.remoteUser

        request.parameterMap.forEach {
            k,v ->
                requestParameters[k] = v.first()
        }

        // request header
        val requestHeaders = HashMap<String, Any>()
        val requestHeaderNameList = request.headerNames
        while (requestHeaderNameList.hasMoreElements()) {
            val headerName = requestHeaderNameList.nextElement()
            requestHeaders.put(headerName, request.getHeader(headerName))
        }

        requestInfos.put("parameters", requestParameters)
        requestInfos.put("headers", requestHeaders)

        return requestInfos
    }
}
