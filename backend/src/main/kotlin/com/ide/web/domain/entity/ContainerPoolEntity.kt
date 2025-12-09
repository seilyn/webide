package com.ide.web.domain.entity


import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "container_pool",
    indexes = [
        Index(name = "idx_status", columnList = "status"),
        Index(name = "idx_assigned_user_id", columnList = "assigned_user_id")
    ]
)
class ContainerPoolEntity(
    @Id
    @Column(name = "pool_id", columnDefinition = "BINARY(16)")
    var poolId: UUID? = null,

    // Docker 컨테이너 정보
    @Column(name = "docker_container_id", unique = true, nullable = false, length = 255)
    var dockerContainerId: String,

    @Column(name = "container_name", unique = true, nullable = false, length = 255)
    var containerName: String,

    // 컨테이너 상태
    @Column(name = "status", nullable = false, length = 20)
    var status: String = "AVAILABLE",  // AVAILABLE, IN_USE, INITIALIZING, ERROR

    // 할당 정보
    @Column(name = "assigned_user_id", columnDefinition = "BINARY(16)")
    var assignedUserId: UUID? = null,

    @Column(name = "assigned_at")
    var assignedAt: LocalDateTime? = null,

    @Column(name = "released_at")
    var releasedAt: LocalDateTime? = null,

    // 사용 통계
    @Column(name = "total_usage_count")
    var totalUsageCount: Int = 0,

    @Column(name = "last_used_at")
    var lastUsedAt: LocalDateTime? = null,

    // 컨테이너 건강 상태
    @Column(name = "health_check_at")
    var healthCheckAt: LocalDateTime? = null,

    @Column(name = "is_healthy")
    var isHealthy: Boolean = true,

    @Column(name = "error_message", columnDefinition = "TEXT")
    var errorMessage: String? = null
) : BaseEntity() {

    @PrePersist
    fun generateIds() {
        if (poolId == null) {
            poolId = UUID.randomUUID()
        }
    }
}
