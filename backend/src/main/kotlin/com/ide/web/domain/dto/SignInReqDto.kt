package com.ide.web.domain.dto

import com.ide.web.common.constant.ValidationMessages
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignInReqDto (

    @field:NotBlank(message = ValidationMessages.EMAIL_REQUIRED)
    @field:Email(message = ValidationMessages.EMAIL_INVALID)
    val email: String,

    @field:NotBlank(message = ValidationMessages.PASSWORD_REQUIRED)
    val password: String
)