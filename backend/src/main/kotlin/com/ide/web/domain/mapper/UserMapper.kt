package com.ide.web.domain.mapper

import com.ide.web.domain.dto.UserResDto
import com.ide.web.domain.entity.UserEntity

fun UserEntity.toDto(): UserResDto =
    UserResDto(
        userPublicId = this.userPublicId,
        email = this.email,
        username = this.username,
        role = this.role,
        planType = this.planType,
        emailVerified = this.emailVerified
    )
