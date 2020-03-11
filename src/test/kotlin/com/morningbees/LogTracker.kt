package com.morningbees

import java.util.Vector

import org.slf4j.LoggerFactory

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender

class LogTracker {


    private val listAppender = ListAppender<ILoggingEvent>()

    private val loggerContext = LoggerFactory
            .getILoggerFactory() as LoggerContext

    private val loggingSources = Vector<Class<*>>()

    private var level: LogTracker.LogLevel = LogTracker.LogLevel.TRACE

    enum class LogLevel private constructor(internal var internalLevel: Level) {
        TRACE(Level.TRACE),
        DEBUG(Level.DEBUG),
        INFO(Level.INFO),
        WARN(Level.WARN),
        ERROR(Level.ERROR)
    }

    fun recordForLevel(level: LogTracker.LogLevel): LogTracker {
        this.level = level
        resetLoggingFramework()
        prepareLoggingFramework()
        return this
    }

    fun recordForObject(sut: Any): LogTracker {
        val type = sut.javaClass
        recordForType(type)
        return this
    }

    fun recordForType(type: Class<*>): LogTracker {
        loggingSources.add(type)
        addAppenderToType(type)
        return this
    }

    operator fun contains(loggingStatement: String): Boolean {
        val list = listAppender.list
        for (event in list) {
            if (event.formattedMessage.contains(loggingStatement)) {
                return true
            }
        }
        return false
    }

    fun size(): Int {
        return listAppender.list.size
    }

    internal fun resetLoggingFramework() {
        listAppender.stop()
        resetLoggingContext()
    }

    internal fun prepareLoggingFramework() {
        resetLoggingContext()
        addAppenderToLoggingSources()
        listAppender.start()
    }

    private fun addAppenderToLoggingSources() {
        for (logSource in loggingSources) {
            addAppenderToType(logSource)
        }
    }

    private fun addAppenderToType(type: Class<*>) {
        val logger = LoggerFactory.getLogger(type) as Logger
        logger.addAppender(listAppender)
        logger.level = level.internalLevel
    }

    private fun resetLoggingContext() {
        loggerContext.reset()
    }

    companion object {


        fun create(): LogTracker {
            return LogTracker()
        }
    }

}
