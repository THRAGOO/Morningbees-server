package com.morningbees.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import javax.servlet.ServletInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.servlet.ReadListener
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class ReadableRequestBodyWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private var bytes: ByteArray
    private var requestBody: HashMap<String, Any>

    init {
        val objectMapper = ObjectMapper()
        val typeRef = object : TypeReference<HashMap<String, Any>>() {}

        bytes = super.getInputStream().readBytes()
        requestBody = objectMapper.readValue<HashMap<String, Any>>(bytes, typeRef)
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        val bis = ByteArrayInputStream(bytes)
        return ServletImpl(bis)
    }

    fun getRequestBody(): HashMap<String, Any> {
        return this.requestBody
    }

    internal inner class ServletImpl(private val bis: InputStream) : ServletInputStream() {
        override fun isReady(): Boolean {
            return true
        }

        override fun isFinished(): Boolean {
            return true
        }

        override fun setReadListener(listener: ReadListener?) {
        }

        @Throws(IOException::class)
        override fun read(): Int {
            return bis.read()
        }

        @Throws(IOException::class)
        override fun read(b: ByteArray): Int {
            return bis.read(b)
        }
    }
}