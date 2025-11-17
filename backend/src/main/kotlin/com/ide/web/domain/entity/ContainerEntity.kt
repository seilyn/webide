package com.ide.web.domain.entity

import com.ide.web.util.NanoIdGenerator
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "containers",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_status", columnList = "status"),
        Index(name = "idx_container_public_id", columnList = "container_public_id")
    ]
)
data class ContainerEntity(
    @Id
    @Column(name = "container_id", columnDefinition = "BINARY(16)")
    var containerId: UUID? = null,

    @Column(name = "container_public_id", unique = true, nullable = false, length = 12, updatable = false)
    var containerPublicId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    var user: UserEntity,

    @Column(name = "container_name", unique = true, nullable = false, length = 255)
    var containerName: String,

    @Column(name = "container_docker_id", unique = true, length = 255)
    var containerDockerId: String? = null,

    @Column(length = 20)
    var status: String = "STOPPED",

    @Column(name = "port_mapping", length = 50)
    var portMapping: String? = null,

    @Column(name = "started_at")
    var startedAt: LocalDateTime? = null,

    @Column(name = "stopped_at")
    var stoppedAt: LocalDateTime? = null,

    @Column(name = "last_used_at")
    var lastUsedAt: LocalDateTime? = null
) : BaseEntity() {

    @PrePersist
    fun generateIds() {
        if (containerId == null) {
            containerId = UUID.randomUUID()
        }
        if (containerPublicId.isEmpty()) {
            containerPublicId = NanoIdGenerator.generate()
        }
    }
}
