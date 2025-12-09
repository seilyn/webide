package com.ide.web.domain.entity

import com.ide.web.common.util.NanoIdGenerator
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(
    name = "execution_histories",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_created_at", columnList = "created_at"),
        Index(name = "idx_execution_public_id", columnList = "execution_public_id")
    ]
)
class ExecutionHistoryEntity(
    @Id
    @Column(name = "execution_id", columnDefinition = "BINARY(16)")
    var executionId: UUID? = null,

    @Column(name = "execution_public_id", unique = true, nullable = false, length = 12, updatable = false)
    var executionPublicId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    var user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", columnDefinition = "BINARY(16)")
    var project: ProjectEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", columnDefinition = "BINARY(16)")
    var file: CodeFileEntity? = null,

    @Column(nullable = false, length = 50)
    var language: String,

    @Lob
    @Column(name = "source_code", nullable = false, columnDefinition = "TEXT")
    var sourceCode: String,

    @Lob
    @Column(name = "input_data", columnDefinition = "TEXT")
    var inputData: String? = null,

    @Lob
    @Column(name = "output_data", columnDefinition = "TEXT")
    var outputData: String? = null,

    @Lob
    @Column(name = "error_message", columnDefinition = "TEXT")
    var errorMessage: String? = null,

    @Column(name = "execution_time_ms")
    var executionTimeMs: Int? = null,

    @Column(name = "timeout_limit_sec")
    var timeoutLimitSec: Int? = null,

    @Column(nullable = false, length = 20)
    var status: String,

    @Column(name = "is_timeout")
    var isTimeout: Boolean = false,

    @Column(name = "cpu_usage_percent", precision = 5, scale = 2)
    var cpuUsagePercent: BigDecimal? = null,

    @Column(name = "memory_usage_mb")
    var memoryUsageMb: Int? = null
) : BaseEntity() {

    @PrePersist
    fun generateIds() {
        if (executionId == null) {
            executionId = UUID.randomUUID()
        }
        if (executionPublicId.isEmpty()) {
            executionPublicId = NanoIdGenerator.generate()
        }
    }
}