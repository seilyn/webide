package com.ide.web.domain.entity

import com.ide.web.domain.entity.BaseEntity
import com.ide.web.domain.entity.ContainerEntity
import com.ide.web.domain.entity.ProjectEntity
import com.ide.web.common.util.NanoIdGenerator
import com.ide.web.domain.dto.UserResDto
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_email", columnList = "email"),
        Index(name = "idx_user_public_id", columnList = "user_public_id")
    ]
)
data class UserEntity(
    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    var userId: UUID? = null,

    @Column(name = "user_public_id", unique = true, nullable = false, length = 12, updatable = false)
    var userPublicId: String = "",

    @Column(unique = true, nullable = false, length = 255)
    var email: String,

    @Column(name = "password_hash", nullable = false, length = 255)
    var passwordHash: String,

    @Column(nullable = false, length = 50)
    var username: String,

    @Column(name = "role", nullable = false, length = 30)
    var role: String = "USER",

    @Column(name = "email_verified", nullable = false)
    var emailVerified: Boolean = false,

    @Column(name = "last_login")
    var lastLogin: LocalDateTime? = null,

    @Column(name = "password_changed_at")
    var passwordChangedAt: LocalDateTime? = null,

    @Column(name = "failed_login_count", nullable = false)
    var failedLoginCount: Int = 0,

    @Column(name = "locked_until")
    var lockedUntil: LocalDateTime? = null,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false,

    @Column(name = "plan_type", length = 30, nullable = false)
    var planType: String = "FREE",

    @Column(name = "storage_limit_mb")
    var storageLimitMb: Int? = null,

    @Column(name = "storage_used_mb")
    var storageUsedMb: Int? = null,

    // Relationships
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val containers: MutableList<ContainerEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val projects: MutableList<ProjectEntity> = mutableListOf(),

) : BaseEntity() {

    @PrePersist
    fun generateIds() {
        if (userId == null) {
            userId = UUID.randomUUID()
        }
        if (userPublicId.isEmpty()) {
            userPublicId = NanoIdGenerator.generate()
        }
    }
}
