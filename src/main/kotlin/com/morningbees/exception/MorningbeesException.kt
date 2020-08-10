package com.morningbees.exception

import java.lang.Exception

open class MorningbeesException : RuntimeException {
    var code: ErrorCode = ErrorCode.BadRequest
    private var logEventCode: String = ""

    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String = ""): super(message) {
        this.code = code
        this.logEventCode = logEventCode
    }
    constructor(message: String, code: ErrorCode, logEventCode: String = "", ex: Exception): super(message, ex) {
        this.code = code
        this.logEventCode = logEventCode
    }
}

class InternalException : MorningbeesException {
    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String = ""): super(message, code, logEventCode)
}

class BadRequestException : MorningbeesException {
    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String = ""): super(message, code, logEventCode)
}

class InvalidParameterException : MorningbeesException {
    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String = ""): super(message, code, logEventCode)
}

class NotFoundException : MorningbeesException {
    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String = ""): super(message, code, logEventCode)
}

class UnAuthorizeException : MorningbeesException {
    constructor(message: String): super(message)
    constructor(message: String, code: ErrorCode, logEventCode: String = ""): super(message, code, logEventCode)
}