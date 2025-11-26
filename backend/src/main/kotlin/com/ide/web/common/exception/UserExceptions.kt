package com.ide.web.common.exception

sealed class UserExceptions(errorCode: ErrorCode) : BaseException(errorCode)

class UserAlreadyExistsException : BaseException(ErrorCode.USER_ALREADY_EXISTS)
class UserNotFoundException : BaseException(ErrorCode.USER_NOT_FOUND)
class UserLockedException : BaseException(ErrorCode.AUTH_ACCOUNT_LOCKED)
class UserInactiveException : BaseException(ErrorCode.USER_INACTIVE)




