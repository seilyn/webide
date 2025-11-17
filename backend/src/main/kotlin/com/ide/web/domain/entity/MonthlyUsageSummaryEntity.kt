package com.ide.web.domain.entity


import com.ide.web.domain.entity.UserEntity
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(
    name = "monthly_usage_summary",
    uniqueConstraints = [
        UniqueConstraint(name = "unique_user_month", columnNames = ["user_id", "year_month"])
    ],
    indexes = [Index(name = "idx_year_month", columnList = "year_month")]
)
data class MonthlyUsageSummaryEntity(

    @Id
    @Column(name = "monthly_usage_summary_id", columnDefinition = "BINARY(16)")
    var executionId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    var user: UserEntity,

    @Column(name = "year_month", nullable = false, length = 7)
    var yearMonth: String,

    @Column(name = "total_executions")
    var totalExecutions: Int = 0,

    @Column(name = "total_execution_time_sec")
    var totalExecutionTimeSec: Long = 0L,

    @Column(name = "avg_cpu_usage_percent", precision = 5, scale = 2)
    var avgCpuUsagePercent: BigDecimal? = null,

    @Column(name = "avg_memory_usage_mb")
    var avgMemoryUsageMb: Int? = null,

    @Column(name = "max_storage_used_mb")
    var maxStorageUsedMb: Int? = null,

    @Column(name = "total_files_created")
    var totalFilesCreated: Int = 0
) : BaseEntity()