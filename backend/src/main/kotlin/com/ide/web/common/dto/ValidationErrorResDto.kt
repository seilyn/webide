package com.ide.web.common.dto

import java.time.LocalDateTime

data class ValidationErrorResDto(
    val status: Int,
    val code: String,
    val message: String,
    val timestamp: LocalDateTime,
    val errors: Map<String, String>
)