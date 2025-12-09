package com.ide.web.common.exception

enum class SuccessCode(
    val code: String,
    val message: String
) {
    AUTH_REGISTER_SUCCESS("AUTH_REGISTER_SUCCESS", "회원가입 성공"),
    AUTH_LOGIN_SUCCESS("AUTH_LOGIN_SUCCESS", "로그인 성공"),
    TOKEN_REFRESH_SUCCESS("TOKEN_REFRESH_SUCCESS", "토큰 재발급 성공"),
    LOGOUT_SUCCESS("LOGOUT_SUCCESS", "로그아웃 성공"),

    USER_PROFILE_FETCH_SUCCESS("USER_PROFILE_FETCH_SUCCESS", "사용자 정보 조회 성공"),
    USER_UPDATE_SUCCESS("USER_UPDATE_SUCCESS", "사용자 정보 수정 성공"),

    // 시스템 공통
    REQUEST_SUCCESS("SUCCESS", "요청 성공")
}
