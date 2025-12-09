package com.ide.web.common.exception

import com.ide.web.common.dto.CommonResDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * 로그인 실패 (이메일/비밀번호 오류) 전용
     * -> failedLoginCount 를 data에 담아서 반환
     */
    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(
        ex: InvalidCredentialsException
    ): ResponseEntity<CommonResDto<Map<String, Any>>> {
        val errorCode = ex.errorCode

        val body: CommonResDto<Map<String, Any>> = CommonResDto.fail(
            errorCode = errorCode,
            data = mapOf<String, Any>(
                "failedLoginCount" to ex.failedLoginCount
            )
        )

        return ResponseEntity.status(errorCode.httpStatus).body(body)
    }

    /**
     * 그 외 커스텀 비즈니스 예외(BaseException)
     */
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(
        ex: BaseException
    ): ResponseEntity<CommonResDto<Nothing>> {
        val errorCode = ex.errorCode

        val body: CommonResDto<Nothing> = CommonResDto.fail(
            errorCode = errorCode,
            data = null
        )

        return ResponseEntity.status(errorCode.httpStatus).body(body)
    }

    /**
     * @Valid 검증 실패
     * -> data에 errors만 넣어줌
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<CommonResDto<Map<String, Any>>> {

        val errors: Map<String, String> =
            ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "") }

        val errorCode = ErrorCode.COMMON_INVALID_PARAMETER

        val body: CommonResDto<Map<String, Any>> = CommonResDto.fail(
            errorCode = errorCode,
            data = mapOf<String, Any>(
                "errors" to errors
            )
        )

        return ResponseEntity.status(errorCode.httpStatus).body(body)
    }

    /**
     * 예측 못한 모든 예외
     */
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        ex: Exception
    ): ResponseEntity<CommonResDto<Nothing>> {
        val errorCode = ErrorCode.COMMON_INTERNAL_ERROR

        val body: CommonResDto<Nothing> = CommonResDto.fail(
            errorCode = errorCode,
            data = null
        )

        return ResponseEntity.status(errorCode.httpStatus).body(body)
    }
}
