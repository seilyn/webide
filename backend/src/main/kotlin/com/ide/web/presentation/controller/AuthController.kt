package com.ide.web.presentation.controller



import com.ide.web.common.dto.CommonResDto
import com.ide.web.common.exception.SuccessCode
import com.ide.web.domain.dto.*
import com.ide.web.domain.service.AuthService
import eu.bitwalker.useragentutils.UserAgent
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignUpReqDto): ResponseEntity<CommonResDto<SignUpResDto>> {
        val response = authService.signup(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                CommonResDto.ok(
                    SuccessCode.AUTH_REGISTER_SUCCESS,  response)
            )
    }
    @PostMapping("/signin")
    fun signin(
        @Valid @RequestBody request: SignInReqDto,
        httpRequest: HttpServletRequest
    ): ResponseEntity<CommonResDto<SignInResDto>> {

        val clientIp = httpRequest.getHeader("X-Forwarded-For")
            ?.split(",")?.firstOrNull()?.trim()
            ?: httpRequest.remoteAddr

        val userAgent = httpRequest.getHeader("User-Agent") ?: "UNKNOWN"

        // UA 파싱해서 os / browser / device 추출하도록 함
        val uaParsed = UserAgent.parseUserAgentString(userAgent)
        val os = uaParsed.operatingSystem?.name   
        val browser = uaParsed.browser?.name  
        val device = uaParsed.operatingSystem?.deviceType?.name 

        val response = authService.signin(
            request = request,
            clientIp = clientIp,
            userAgent = userAgent,
            os = os,
            browser = browser,
            device = device
        )

        return ResponseEntity.ok(
            CommonResDto.ok(SuccessCode.AUTH_LOGIN_SUCCESS, response)
        )
    }

}
