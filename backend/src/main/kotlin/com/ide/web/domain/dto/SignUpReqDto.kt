package com.ide.web.domain.dto

import com.ide.web.common.constant.ValidationMessages
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class SignUpReqDto (
    @field:NotBlank(message = ValidationMessages.EMAIL_REQUIRED)
    @field:Email(message = ValidationMessages.EMAIL_INVALID)
    val email: String,

    @field:NotBlank(message = ValidationMessages.PASSWORD_REQUIRED)
    @field:Size(min = 8, message = ValidationMessages.PASSWORD_SIZE)
    val password: String,

    @field:NotBlank(message = ValidationMessages.USERNAME_REQUIRED)
    @field:Size(min = 2, max = 50, message = ValidationMessages.USERNAME_SIZE)
    val username: String
)