package com.ide.web.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "login_history",
    indexes = [
        Index(name = "idx_login_user", columnList = "user_id"),
        Index(name = "idx_login_time", columnList = "login_at")
    ]
)
class LoginHistoryEntity(
    @Id
    @GeneratedValue
    @Column(name = "login_history_id", columnDefinition = "BINARY(16)")
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    var user: UserEntity,

    @Column(name = "ip_address", length = 45)
    var ipAddress: String? = null,    // IPv6까지 고려

    @Column(name = "user_agent", length = 500)
    var userAgent: String? = null,

    @Column(name = "os", length = 50)
    var os: String? = null,

    @Column(name = "browser", length = 100)
    var browser: String? = null,

    @Column(name = "device", length = 100)
    var device: String? = null,       // mobile, desktop 등

    @Column(name = "login_at", nullable = false)
    var loginAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "is_success", nullable = false)
    var isSuccess: Boolean = true,

    @Column(name = "fail_reason", length = 255)
    var failReason: String? = null

): BaseEntity() {
}