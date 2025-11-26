package com.ide.web.common.exception

open class BaseException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)
