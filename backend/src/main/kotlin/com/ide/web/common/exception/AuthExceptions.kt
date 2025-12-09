package com.ide.web.common.exception

sealed class AuthExceptions(errorCode: ErrorCode) : BaseException(errorCode)

class InvalidCredentialsException(
    val failedLoginCount: Int
) : BaseException(ErrorCode.AUTH_INVALID_CREDENTIALS)

class TokenExpiredException : BaseException(ErrorCode.AUTH_TOKEN_EXPIRED)
class InvalidTokenException : BaseException(ErrorCode.AUTH_TOKEN_INVALID)

