package com.morningbees.exception

import org.slf4j.Logger
import java.lang.Exception

open class MorningbeesException : RuntimeException {
    var code: ErrorCode = ErrorCode.BadRequest
    var logEventCode: String = ""
    lateinit var logger: Logger

    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String = ""): super(message) {
        this.code = code
        this.logEventCode = logEventCode
    }
    constructor(message: String, code: ErrorCode, logEventCode: String = "", logger: Logger): super(message) {
        this.code = code
        this.logEventCode = logEventCode
        this.logger = logger
    }
    constructor(message: String, code: ErrorCode, logEventCode: String = "", ex: Exception): super(message, ex) {
        this.code = code
        this.logEventCode = logEventCode
    }
}

class InternalException : MorningbeesException {
    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String): super(message, code, logEventCode)
    constructor(message: String, code: ErrorCode, logEventCode: String, logger: Logger): super(message, code, logEventCode, logger)
}

class BadRequestException : MorningbeesException {
    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String): super(message, code, logEventCode)
    constructor(message: String, code: ErrorCode, logEventCode: String, logger: Logger): super(message, code, logEventCode, logger)
}

class UnAuthorizeException : MorningbeesException {
    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String): super(message, code, logEventCode)
    constructor(message: String, code: ErrorCode, logEventCode: String, logger: Logger): super(message, code, logEventCode, logger)
}