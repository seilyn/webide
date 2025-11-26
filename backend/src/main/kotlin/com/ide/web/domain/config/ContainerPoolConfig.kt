package com.ide.web.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * 컨테이너 풀 섷정
 */
@Configuration
@ConfigurationProperties(prefix = "container.pool")
data class ContainerPoolConfig(
    var initialSize: Int = 20,              // 시작 시 20개
    var minIdle: Int = 10,                  // 최소 10개 대기
    var maxSize: Int = 250,                 // 최대 250개로함

    // 오토스케일링
    var scaleUpThreshold: Double = 0.8,     // 80% 사용 시 증가
    var scaleDownThreshold: Double = 0.3,   // 30% 이하 시 감소
    var scaleUpCount: Int = 10,             // 한 번에 10개 증가
    var scaleDownCount: Int = 5,            // 한 번에 5개 감소

    // 타임아웃
    var maxIdleTimeMinutes: Int = 15,       // 15분 유휴 시 반환
    var maxSessionTimeHours: Int = 8,       // 8시간 세션 제한

    // 청소
    var aggressiveCleanup: Boolean = true
)