package com.ide.web.common.dto

import com.ide.web.common.exception.ErrorCode
import com.ide.web.common.exception.SuccessCode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class CommonResDto<T>(
    val success: Boolean,
    val code: String,
    val message: String,
    val status: Int,
    val timestamp: String,
    val data: T? = null
) {
    companion object {

        fun <T> ok(
            successCode: SuccessCode,
            data: T? = null,
            status: Int = 200
        ): CommonResDto<T> =
            CommonResDto(
                success = true,
                code = successCode.code,
                message = successCode.message,
                status = status,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                data = data
            )

        fun <T> fail(
            errorCode: ErrorCode,
            data: T? = null
        ): CommonResDto<T> =
            CommonResDto(
                success = false,
                code = errorCode.code,
                message = errorCode.message,
                status = errorCode.httpStatus.value(),
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                data = data
            )
    }
}
