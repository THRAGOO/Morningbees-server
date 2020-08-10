package com.morningbees

import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext

class LogTrackerStub private constructor()//hide the constructor to force the use of the "create" method
    : BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private var logTracker = LogTracker.create()

    @Throws(Exception::class)
    override fun beforeTestExecution(context: ExtensionContext) {
        logTracker.prepareLoggingFramework()
    }

    @Throws(Exception::class)
    override fun afterTestExecution(context: ExtensionContext) {
        logTracker.resetLoggingFramework()
    }

    fun recordForLevel(level: LogTracker.LogLevel): LogTrackerStub {
        logTracker.recordForLevel(level)
        return this
    }

    fun recordForObject(sut: Any): LogTrackerStub {
        logTracker.recordForObject(sut)
        return this
    }

    fun recordForType(type: Class<*>): LogTrackerStub {
        logTracker.recordForType(type)
        return this
    }

    operator fun contains(loggingStatement: String): Boolean {
        return logTracker.contains(loggingStatement)
    }

    fun size(): Int {
        return logTracker.size()
    }

    companion object {

        fun create(): LogTrackerStub {
            return LogTrackerStub()
        }
    }

}