package com.ide.web.common.exception

import com.ide.web.common.dto.ErrorResDto
import com.ide.web.common.dto.ValidationErrorResDto
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): ResponseEntity<ErrorResDto> {
        val errorCode = ex.errorCode
        val error = ErrorResDto(
            status = errorCode.httpStatus.value(),
            code = errorCode.code,
            message = errorCode.message,
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity.status(errorCode.httpStatus).body(error)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResDto> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "유효하지 않은 값입니다."
            errors[fieldName] = errorMessage
        }

        val error = ValidationErrorResDto(
            status = ErrorCode.COMMON_INVALID_PARAMETER.httpStatus.value(),
            code = ErrorCode.COMMON_INVALID_PARAMETER.code,
            message = ErrorCode.COMMON_INVALID_PARAMETER.message,
            timestamp = LocalDateTime.now(),
            errors = errors
        )
        return ResponseEntity.badRequest().body(error)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception): ResponseEntity<ErrorResDto> {
        val error = ErrorResDto(
            status = ErrorCode.COMMON_INTERNAL_ERROR.httpStatus.value(),
            code = ErrorCode.COMMON_INTERNAL_ERROR.code,
            message = ErrorCode.COMMON_INTERNAL_ERROR.message,
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity.status(ErrorCode.COMMON_INTERNAL_ERROR.httpStatus).body(error)
    }
}
