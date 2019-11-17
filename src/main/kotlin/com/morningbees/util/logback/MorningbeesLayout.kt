package com.morningbees.util.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.json.classic.JsonLayout;
import com.morningbees.util.RequestInfoLogging
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.StringTokenizer
import java.util.TreeMap

class MorningbeesLayout : JsonLayout() {
    override fun addCustomDataToJsonMap(map: MutableMap<String, Any>?, event: ILoggingEvent?) {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

        val logging = RequestInfoLogging()
        val requestInfos = logging.makeLoggingRequestMap(request)

        map?.put("request", requestInfos)

        super.addCustomDataToJsonMap(map, event)
    }
}