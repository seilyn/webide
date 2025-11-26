package com.ide.web.presentation.controller



import com.ide.web.domain.dto.SignInReqDto
import com.ide.web.domain.dto.SignInResDto
import com.ide.web.domain.dto.SignUpReqDto
import com.ide.web.domain.dto.SignUpResDto
import com.ide.web.domain.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignUpReqDto): ResponseEntity<SignUpResDto> {
        val response = authService.signup(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/signin")
    fun login(@Valid @RequestBody request: SignInReqDto): ResponseEntity<SignInResDto> {
        val response = authService.signin(request)
        return ResponseEntity.ok(response)
    }

//    @PostMapping("/refresh")
//    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<> {
//          TODO: 리프레시토큰 로직구현할것
//    }

//    @PostMapping("/logout")
//    fun logout(): ResponseEntity<> {
//        TODO: 로그아웃로직 구현
//    }
}