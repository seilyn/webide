package com.ide.web.domain.entity


import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "supported_languages")
class SupportedLanguageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    val languageId: Int? = null,

    @Column(name = "language_code", unique = true, nullable = false, length = 50)
    var languageCode: String,

    @Column(name = "language_name", nullable = false, length = 100)
    var languageName: String,

    @Column(length = 50)
    var version: String? = null,

    @Column(name = "docker_image", nullable = false, length = 255)
    var dockerImage: String,

    @Lob
    @Column(name = "compile_command", columnDefinition = "TEXT")
    var compileCommand: String? = null,

    @Lob
    @Column(name = "execute_command", nullable = false, columnDefinition = "TEXT")
    var executeCommand: String,

    @Column(name = "file_extension", nullable = false, length = 10)
    var fileExtension: String,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null
)