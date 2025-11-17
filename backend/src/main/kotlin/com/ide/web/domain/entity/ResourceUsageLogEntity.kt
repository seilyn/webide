package com.ide.web.domain.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(
    name = "resource_usage_logs",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_created_at", columnList = "created_at"),
        Index(name = "idx_container_id", columnList = "container_id")
    ]
)
data class ResourceUsageLogEntity(

    @Id
    @Column(name = "resource_usage_log_id", columnDefinition = "BINARY(16)")
    var executionId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    var user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id", columnDefinition = "BINARY(16)")
    var container: ContainerEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_id", columnDefinition = "BINARY(16)")
    var execution: ExecutionHistoryEntity? = null,

    @Column(name = "cpu_usage_percent", precision = 5, scale = 2)
    var cpuUsagePercent: BigDecimal? = null,

    @Column(name = "memory_usage_mb")
    var memoryUsageMb: Int? = null,

    @Column(name = "storage_usage_mb")
    var storageUsageMb: Int? = null,

    @Column(name = "execution_duration_ms")
    var executionDurationMs: Int? = null
) : BaseEntity()
