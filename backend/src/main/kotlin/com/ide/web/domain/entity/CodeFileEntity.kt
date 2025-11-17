package com.ide.web.domain.entity

import com.ide.web.util.NanoIdGenerator
import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "code_files",
    indexes = [
        Index(name = "idx_project_id", columnList = "project_id"),
        Index(name = "idx_file_size", columnList = "file_size_bytes"),
        Index(name = "idx_file_public_id", columnList = "file_public_id")
    ]
)
data class CodeFileEntity(
    @Id
    @Column(name = "file_id", columnDefinition = "BINARY(16)")
    var fileId: UUID? = null,

    @Column(name = "file_public_id", unique = true, nullable = false, length = 12, updatable = false)
    var filePublicId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, columnDefinition = "BINARY(16)")
    var project: ProjectEntity,

    @Column(name = "file_name", nullable = false, length = 255)
    var fileName: String,

    @Column(name = "file_path", length = 500)
    var filePath: String? = null,

    @Lob
    @Column(name = "source_code", nullable = false, columnDefinition = "LONGTEXT")
    var sourceCode: String,

    @Column(name = "file_size_bytes")
    var fileSizeBytes: Long = 0L,

    @Column(nullable = false, length = 50)
    var language: String
) : BaseEntity() {

    @PrePersist
    fun generateIds() {
        if (fileId == null) {
            fileId = UUID.randomUUID()
        }
        if (filePublicId.isEmpty()) {
            filePublicId = NanoIdGenerator.generate()
        }
    }
}