package com.ide.web.domain.dto

data class SignInResDto (
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: UserResDto
)