package com.morningbees.util.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.json.classic.JsonLayout
import com.morningbees.util.RequestInfoLogging
import net.logstash.logback.marker.ObjectAppendingMarker
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class MorningbeesLayout : JsonLayout() {
    override fun addCustomDataToJsonMap(map: MutableMap<String, Any>?, event: ILoggingEvent?) {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

        val logging = RequestInfoLogging()
        val requestInfos = logging.makeLoggingRequestMap(request)

        val additionalInfos: HashMap<String, Any> = HashMap()

        event?.argumentArray?.forEach {
            value ->
                val logArgument = (value as ObjectAppendingMarker)
            additionalInfos[logArgument.fieldName] = logArgument.fieldValue
        }

        map?.put("request", requestInfos)
        map?.put("additionalInfos", additionalInfos)

        super.addCustomDataToJsonMap(map, event)
    }
}