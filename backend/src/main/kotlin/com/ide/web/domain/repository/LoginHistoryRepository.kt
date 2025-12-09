package com.ide.web.domain.repository

import com.ide.web.domain.entity.LoginHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LoginHistoryRepository : JpaRepository<LoginHistoryEntity, UUID>
