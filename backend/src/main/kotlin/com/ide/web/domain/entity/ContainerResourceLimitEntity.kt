package com.ide.web.domain.entity


import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(
    name = "container_resource_limits",
    uniqueConstraints = [
        UniqueConstraint(name = "unique_user_container", columnNames = ["user_id", "container_id"])
    ],
    indexes = [Index(name = "idx_user_id", columnList = "user_id")]
)
data class ContainerResourceLimitEntity(

    @Id
    @Column(name = "container_resource_limit_id", columnDefinition = "BINARY(16)")
    var executionId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    var user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id", columnDefinition = "BINARY(16)")
    var container: ContainerEntity? = null,

    @Column(name = "cpu_limit", precision = 4, scale = 2)
    var cpuLimit: BigDecimal = BigDecimal("1.0"),

    @Column(name = "memory_limit_mb")
    var memoryLimitMb: Int = 512,

    @Column(name = "storage_limit_mb")
    var storageLimitMb: Int = 1024,

    @Column(name = "network_limit_mbps")
    var networkLimitMbps: Int = 10,

    @Column(name = "max_execution_time_sec")
    var maxExecutionTimeSec: Int = 30,

    @Column(name = "max_file_size_mb")
    var maxFileSizeMb: Int = 10,

    @Column(name = "concurrent_executions")
    var concurrentExecutions: Int = 1
) : BaseEntity()