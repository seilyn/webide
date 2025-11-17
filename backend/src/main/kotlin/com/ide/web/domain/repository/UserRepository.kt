package com.ide.web.domain.repository

import com.ide.web.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(email: String): UserEntity?
    fun findByUserPublicId(userPublicId: String): UserEntity?
    fun existsByEmail(email: String): Boolean
}