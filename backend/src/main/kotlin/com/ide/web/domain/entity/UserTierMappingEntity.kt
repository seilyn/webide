package com.ide.web.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "user_tier_mapping",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_expires_at", columnList = "expires_at")
    ]
)
class UserTierMappingEntity(

    @Id
    @Column(name = "mapping_id", columnDefinition = "BINARY(16)")
    var mappingId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    var user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id", nullable = false)
    var tier: UserTierEntity,

    @Column(name = "expires_at")
    var expiresAt: LocalDateTime? = null,

    @Column(name = "is_active")
    var isActive: Boolean = true
) : BaseEntity()
