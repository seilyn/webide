package com.ide.web.domain.service

import com.ide.web.common.exception.InvalidCredentialsException
import com.ide.web.common.exception.UserAlreadyExistsException
import com.ide.web.common.exception.UserLockedException
import com.ide.web.common.exception.UserNotFoundException
import com.ide.web.domain.dto.SignInReqDto
import com.ide.web.domain.dto.SignInResDto
import com.ide.web.domain.dto.SignUpReqDto
import com.ide.web.domain.dto.SignUpResDto
import com.ide.web.domain.entity.LoginHistoryEntity
import com.ide.web.domain.entity.UserEntity
import com.ide.web.domain.mapper.toDto
import com.ide.web.domain.repository.LoginHistoryRepository
import com.ide.web.domain.repository.UserRepository
import com.ide.web.infrastructure.security.JwtUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var jwtUtil: JwtUtil

    @Mock
    lateinit var authenticationManager: AuthenticationManager

    @Mock
    lateinit var loginHistoryRepository: LoginHistoryRepository

    @Captor
    lateinit var loginHistoryCaptor: ArgumentCaptor<LoginHistoryEntity>

    lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = AuthService(
            userRepository = userRepository,
            passwordEncoder = passwordEncoder,
            jwtUtil = jwtUtil,
            authenticationManager = authenticationManager,
            loginHistoryRepository = loginHistoryRepository
        )
    }

    // ---------- helper ----------

    private fun createUser(
        email: String = "test@test.com",
        failedCount: Int = 0,
        lockedUntil: LocalDateTime? = null
    ): UserEntity {
        val user = UserEntity(
            email = email,
            passwordHash = "encoded-password",
            username = "tester",
            role = "USER",
            planType = "FREE",
            storageLimitMb = 100,
            storageUsedMb = 0
        )
        user.failedLoginCount = failedCount
        user.lockedUntil = lockedUntil
        return user
    }

    // =====================================================================
    // 1. signup 테스트
    // =====================================================================
    @Nested
    inner class SignupTests {

        @Test
        fun `이미 존재하는 이메일이면 UserAlreadyExistsException`() {
            // given
            val req = SignUpReqDto(
                email = "dup@test.com",
                password = "1234",
                username = "dup"
            )
            whenever(userRepository.existsByEmail(req.email)).thenReturn(true)

            // when & then
            assertThrows<UserAlreadyExistsException> {
                authService.signup(req)
            }
            verify(userRepository, never()).save(any())
        }

        @Test
        fun `정상 회원가입시 비밀번호 암호화 및 저장`() {
            // given
            val req = SignUpReqDto(
                email = "new@test.com",
                password = "plain",
                username = "new-user"
            )
            whenever(userRepository.existsByEmail(req.email)).thenReturn(false)
            whenever(passwordEncoder.encode(req.password)).thenReturn("encoded")
            whenever(userRepository.save(any())).thenAnswer { it.arguments[0] as UserEntity }

            // when
            val res: SignUpResDto = authService.signup(req)

            // then
            assertEquals(req.email, res.user.email)
            assertEquals(req.username, res.user.username)
            verify(passwordEncoder).encode("plain")
            verify(userRepository).save(check {
                assertEquals("encoded", it.passwordHash)
                assertEquals("FREE", it.planType)
                assertEquals(100, it.storageLimitMb)
            })
        }
    }

    // =====================================================================
    // 2. signin 테스트
    // =====================================================================
    @Nested
    inner class SigninTests {

        @Test
        fun `유저가 없으면 UserNotFoundException`() {
            // given
            val req = SignInReqDto(email = "none@test.com", password = "1234")
            whenever(userRepository.findByEmail(req.email)).thenReturn(null)

            // when & then
            assertThrows<UserNotFoundException> {
                authService.signin(req, null, null, null, null, null)
            }
            verify(authenticationManager, never()).authenticate(any())
        }

        @Test
        fun `계정이 잠겨있고 lockedUntil 미래면 UserLockedException`() {
            // given
            val req = SignInReqDto(email = "lock@test.com", password = "1234")
            val user = createUser(
                email = req.email,
                lockedUntil = LocalDateTime.now().plusMinutes(10)
            )
            whenever(userRepository.findByEmail(req.email)).thenReturn(user)

            // when & then
            assertThrows<UserLockedException> {
                authService.signin(req, null, null, null, null, null)
            }
            verify(authenticationManager, never()).authenticate(any())
        }

        @Test
        fun `잠금시간이 지났으면 잠금 해제 후 로그인 진행`() {
            // given
            val req = SignInReqDto(email = "unlock@test.com", password = "1234")
            val user = createUser(
                email = req.email,
                failedCount = 3,
                lockedUntil = LocalDateTime.now().minusMinutes(1)
            )
            whenever(userRepository.findByEmail(req.email)).thenReturn(user)
            whenever(authenticationManager.authenticate(any())).thenReturn(mock<Authentication>())
            whenever(jwtUtil.generateToken(any())).thenReturn("access")
            whenever(jwtUtil.generateRefreshToken(any())).thenReturn("refresh")

            // when
            val res: SignInResDto = authService.signin(req, null, null, null, null, null)

            // then
            assertEquals("access", res.accessToken)
            assertEquals("refresh", res.refreshToken)
            assertEquals(0, user.failedLoginCount)
            assertNull(user.lockedUntil)
            verify(userRepository, atLeastOnce()).save(user)
        }

        @Test
        fun `로그인 성공시 lastLogin 갱신 및 실패횟수 초기화`() {
            // given
            val req = SignInReqDto(email = "ok@test.com", password = "1234")
            val user = createUser(email = req.email, failedCount = 2)
            whenever(userRepository.findByEmail(req.email)).thenReturn(user)
            whenever(authenticationManager.authenticate(any())).thenReturn(mock<Authentication>())
            whenever(jwtUtil.generateToken(any())).thenReturn("access-token")
            whenever(jwtUtil.generateRefreshToken(any())).thenReturn("refresh-token")

            // when
            val res = authService.signin(req, "127.0.0.1", "UA", "OS", "BR", "DEV")

            // then
            assertEquals("access-token", res.accessToken)
            assertEquals("refresh-token", res.refreshToken)
            assertEquals(0, user.failedLoginCount)
            assertNotNull(user.lastLogin)
            verify(userRepository, atLeastOnce()).save(user)
            verify(loginHistoryRepository).save(any())
        }

        @Test
        fun `비밀번호 틀리면 실패횟수 증가하고 InvalidCredentialsException`() {
            // given
            val req = SignInReqDto(email = "fail@test.com", password = "wrong")
            val user = createUser(email = req.email, failedCount = 2)
            whenever(userRepository.findByEmail(req.email)).thenReturn(user)
            whenever(authenticationManager.authenticate(any()))
                .thenThrow(object : AuthenticationException("bad") {})

            // when
            val ex = assertThrows<InvalidCredentialsException> {
                authService.signin(req, "1.1.1.1", "UA", null, null, null)
            }

            // then
            assertEquals(3, user.failedLoginCount)
            assertEquals(3, ex.failedLoginCount) // (필드명은 실제 클래스에 맞춰 수정)
            verify(userRepository, atLeastOnce()).save(user)
            verify(loginHistoryRepository).save(any())
        }

        @Test
        fun `실패횟수가 maxFailedAttempts 이상이면 lockedUntil 설정`() {
            // given
            val req = SignInReqDto(email = "lock2@test.com", password = "wrong")
            val user = createUser(email = req.email, failedCount = 4) // 기본 maxFailedAttempts = 5
            whenever(userRepository.findByEmail(req.email)).thenReturn(user)
            whenever(authenticationManager.authenticate(any()))
                .thenThrow(object : AuthenticationException("bad") {})

            // when
            assertThrows<InvalidCredentialsException> {
                authService.signin(req, null, null, null, null, null)
            }

            // then
            assertEquals(5, user.failedLoginCount)
            assertNotNull(user.lockedUntil)
            verify(userRepository, atLeastOnce()).save(user)
        }

        @Test
        fun `로그인 성공시 LoginHistory에 성공 기록 저장`() {
            // given
            val req = SignInReqDto(email = "history@test.com", password = "1234")
            val user = createUser(email = req.email)
            whenever(userRepository.findByEmail(req.email)).thenReturn(user)
            whenever(authenticationManager.authenticate(any())).thenReturn(mock<Authentication>())
            whenever(jwtUtil.generateToken(any())).thenReturn("access")
            whenever(jwtUtil.generateRefreshToken(any())).thenReturn("refresh")

            // when
            authService.signin(req, "10.0.0.1", "UA", "OS", "BR", "DEV")

            // then
            verify(loginHistoryRepository).save(capture(loginHistoryCaptor))
            val history = loginHistoryCaptor.value
            assertTrue(history.isSuccess)
            assertNull(history.failReason)
            assertEquals("10.0.0.1", history.ipAddress)
        }

        @Test
        fun `로그인 실패시 LoginHistory에 실패 기록 저장`() {
            // given
            val req = SignInReqDto(email = "history-fail@test.com", password = "bad")
            val user = createUser(email = req.email)
            whenever(userRepository.findByEmail(req.email)).thenReturn(user)
            whenever(authenticationManager.authenticate(any()))
                .thenThrow(object : AuthenticationException("bad") {})

            // when
            assertThrows<InvalidCredentialsException> {
                authService.signin(req, "20.0.0.1", "UA", "OS", "BR", "DEV")
            }

            // then
            verify(loginHistoryRepository).save(capture(loginHistoryCaptor))
            val history = loginHistoryCaptor.value
            assertFalse(history.isSuccess)
            assertEquals("20.0.0.1", history.ipAddress)
            assertNotNull(history.failReason)
        }
    }
}
