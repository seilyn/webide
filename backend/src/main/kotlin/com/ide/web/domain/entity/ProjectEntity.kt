package com.ide.web.domain.entity

import com.ide.web.common.util.NanoIdGenerator
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "projects",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_project_public_id", columnList = "project_public_id"),
        Index(name = "idx_project_public_id", columnList = "project_public_id"),
    ])
class ProjectEntity(
    @Id
    @Column(name = "project_id", columnDefinition = "BINARY(16)")
    var projectId: UUID? = null,

    @Column(name = "project_public_id", unique = true, nullable = false, length = 12, updatable = false)
    var projectPublicId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    var user: UserEntity,

    @Column(name = "project_name", nullable = false, length = 100)
    var projectName: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,


    @OneToMany(mappedBy = "project", cascade = [CascadeType.ALL], orphanRemoval = true)
    val codeFiles: MutableList<CodeFileEntity> = mutableListOf(),

    @OneToMany(mappedBy = "project", cascade = [CascadeType.ALL])
    val executionHistories: MutableList<ExecutionHistoryEntity> = mutableListOf()
) : BaseEntity() {
    @PrePersist
    fun generateIds() {
        if (projectId == null) {
            projectId = UUID.randomUUID()
        }
        if (projectPublicId.isEmpty()) {
            projectPublicId = NanoIdGenerator.generate()
        }
    }

    // 프로젝트에서 사용된 언어 목록 조회
    fun getUsedLanguages(): Set<String> {
        return codeFiles.map { it.language }.toSet()
    }

    // 특정 언어의 파일 개수
    fun getFileCountByLanguage(language: String): Int {
        return codeFiles.count { it.language == language }
    }
}