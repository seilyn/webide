package com.ide.web.domain.entity


import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "user_tiers")
data class UserTierEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    val tierId: Int? = null,

    @Column(name = "tier_name", unique = true, nullable = false, length = 50)
    var tierName: String,

    @Column(name = "tier_display_name", nullable = false, length = 100)
    var tierDisplayName: String,

    @Column(name = "cpu_limit", precision = 4, scale = 2)
    var cpuLimit: BigDecimal = BigDecimal("0.5"),

    @Column(name = "memory_limit_mb")
    var memoryLimitMb: Int = 256,

    @Column(name = "storage_limit_mb")
    var storageLimitMb: Int = 500,

    @Column(name = "max_execution_time_sec")
    var maxExecutionTimeSec: Int = 10,

    @Column(name = "max_file_size_mb")
    var maxFileSizeMb: Int = 5,

    @Column(name = "max_projects")
    var maxProjects: Int = 3,

    @Column(name = "concurrent_executions")
    var concurrentExecutions: Int = 1,

    @Column(name = "max_containers")
    var maxContainers: Int = 1,

    @Column(name = "monthly_execution_limit")
    var monthlyExecutionLimit: Int = 100,

    @Column(name = "price_monthly", precision = 10, scale = 2)
    var priceMonthly: BigDecimal = BigDecimal.ZERO,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null
)