package com.ide.web.domain.service

import com.ide.web.common.exception.*
import com.ide.web.domain.dto.*
import com.ide.web.domain.entity.LoginHistoryEntity
import com.ide.web.domain.entity.UserDetailsEntity
import com.ide.web.domain.entity.UserEntity
import com.ide.web.domain.mapper.toDto
import com.ide.web.domain.repository.LoginHistoryRepository
import com.ide.web.domain.repository.UserRepository
import com.ide.web.infrastructure.security.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager,
    private val loginHistoryRepository: LoginHistoryRepository,
) {
    @Value("\${jwt.expiration}")
    private var jwtExpiration: Long = 86400000

    @Value("\${security.max-failed-attempts:5}")
    private var maxFailedAttempts: Int = 5

    @Value("\${security.lock-duration-minutes:30}")
    private var lockDurationMinutes: Long = 30

    /**
     * 회원가입 처리
     *
     * - 중복 이메일 계정이 존재하는지 검증
     * - 비밀번호 암호화 후 신규 사용자 생성
     * - 기본 플랜(FREE) 및 스토리지 제한 설정
     * - Access Token 및 Refresh Token 발급
     *
     * @param request 회원가입 요청 DTO (email, password, username 포함)
     * @return SignUpResDto 발급된 토큰, 만료 시간, 사용자 정보가 포함된 응답 DTO
     * @throws UserAlreadyExistsException 요청한 이메일이 이미 존재하는 경우
     */
    @Transactional
    fun signup(request: SignUpReqDto): SignUpResDto {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException()
        }

        val user = UserEntity(
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            username = request.username,
            role = "USER",
            planType = "FREE",
            storageLimitMb = 100,
            storageUsedMb = 0
        )

        val savedUser = userRepository.save(user)

        return SignUpResDto(
            user = savedUser.toDto()
        )
    }

    /**
     * 로그인 처리
     */
    //@Transactional
    @Transactional(noRollbackFor = [InvalidCredentialsException::class])
    fun signin(request: SignInReqDto,
               clientIp: String?,
               userAgent: String?,
               os: String?,
               browser: String?,
               device: String?): SignInResDto {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException()

        checkAccountLock(user)

        try {

            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )

            resetFailedAttempts(user)
            updateLastLogin(user)
            recordLoginHistory(user, clientIp, userAgent, os, browser, device, success = true, failReason = null)

            val userDetails = UserDetailsEntity(user)
            val token = jwtUtil.generateToken(userDetails)
            val refreshToken = jwtUtil.generateRefreshToken(userDetails)

            return SignInResDto(
                accessToken = token,
                refreshToken = refreshToken,
                expiresIn = jwtExpiration / 1000,
                user = user.toDto()
            )
        } catch (e: AuthenticationException) {
            val failedCount = handleFailedLogin(user)
            recordLoginHistory(
                user,
                clientIp,
                userAgent,
                os,
                browser,
                device,
                success = false,
                failReason = ErrorCode.AUTH_INVALID_CREDENTIALS.code
            )
            throw InvalidCredentialsException(failedCount)
        }


    }
    /**
     * 계정 잠금 여부를 확인하고 필요 시 잠금 해제 처리
     *
     * - lockedUntil 이 현재 시각 이후라면 계정이 잠긴 상태이므로 로그인 차단
     * - lockedUntil 이 지났다면(만료) 잠금을 해제하고 실패 횟수를 초기화
     *
     * @param user 잠금 상태를 검사할 사용자 엔티티
     * @throws UserLockedException 계정이 잠겨 있는 상태일 경우 발생
     * @return Unit
     */
    private fun checkAccountLock(user: UserEntity) {
        user.lockedUntil?.let {lockedUntil ->
            if (lockedUntil.isAfter(LocalDateTime.now())) {
                throw UserLockedException()
            } else {
              user.lockedUntil = null
              user.failedLoginCount = 0
              userRepository.save(user)
            }
        }
    }

    /**
     * 로그인 실패 처리
     * - 실패 횟수를 1 증가시키고, 최대 실패 횟수에 도달한 경우 계정 잠금 시간 설정
     *
     * @param user 로그인 실패 처리 대상 사용자 엔티티
     * @return Unit
     */
    private fun handleFailedLogin(user: UserEntity): Int {
        user.failedLoginCount++

        if (user.failedLoginCount >= maxFailedAttempts) {
            user.lockedUntil = LocalDateTime.now().plusMinutes(lockDurationMinutes)
        }

        userRepository.save(user)
        return user.failedLoginCount
    }


    /**
     * 사용자 최종 로그인 시간을 업데이트
     * - 로그인 성공 시 호출
     *
     * @param user 로그인 성공한 사용자 엔티티
     * @return Unit
     */
    private fun updateLastLogin(user: UserEntity) {
        user.lastLogin = LocalDateTime.now()
        userRepository.save(user)
    }

    /**
     * 로그인 실패 횟수 초기화
     *
     * - 로그인 성공 시 호출
     * - 불필요한 UPDATE 방지를 위해 실패 횟수가 0 이상일 때만 DB 업데이트 수행
     * @param user 실패 횟수를 초기화할 사용자 엔티티
     * @return Unit
     */
    private fun resetFailedAttempts(user: UserEntity) {
        if (user.failedLoginCount > 0) {
            user.failedLoginCount = 0
            user.lockedUntil = null
            userRepository.save(user)
        }
    }

    /**
     * 로그인 시 기록용 함수
     */
    private fun recordLoginHistory(
        user: UserEntity,
        ip: String?,
        ua: String?,
        os: String? = null,
        browser: String? = null,
        device: String? = null,
        success: Boolean,
        failReason: String? = null
    ) {
        val history = LoginHistoryEntity(
            user = user,
            ipAddress = ip,
            userAgent = ua,
            os = os,
            browser = browser,
            device = device,
            isSuccess = success,
            failReason = failReason
        )

        loginHistoryRepository.save(history)
    }

}