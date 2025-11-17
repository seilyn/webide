package com.ide.web.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

// 테이블 x, 자식에게 컬럼만 상속함
@MappedSuperclass
abstract class BaseEntity (
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    open val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    open val updatedAt: LocalDateTime? = null
)