package com.ide.web.domain.dto

data class UserResDto (
    val userPublicId: String,
    val email: String,
    val username: String,
    val role: String,
    val planType: String,
    val emailVerified: Boolean
)