package com.ide.web.common.dto

import java.time.LocalDateTime

class ErrorResDto (
    val status: Int,
    val code: String,
    val message: String,
    val timestamp: LocalDateTime
)