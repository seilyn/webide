package com.ide.web.common.exception

import org.springframework.http.HttpStatus

/**
 * 에러 코드 정의
 * 규칙: {도메인}_{상세분류}_{일련번호}
 *
 * 도메인 코드:
 * - AUTH: 인증/인가
 * - USER: 사용자 관리
 * - PROJECT: 프로젝트
 * - CONTAINER: 컨테이너
 * - COMMON: 공통
 * 
 *  DB로 따로 빼지 않고 그냥 Enum에 넣는다. 얼마 안됨
 */
enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    // ========== 인증/인가 관련 (AUTH_xxx) ==========
    AUTH_INVALID_CREDENTIALS("AUTH_LOGIN_001", "이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_ACCOUNT_LOCKED("AUTH_LOGIN_002", "계정이 잠겨있습니다. 잠시 후 다시 시도해주세요.", HttpStatus.LOCKED),
    AUTH_TOKEN_INVALID("AUTH_TOKEN_001", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_EXPIRED("AUTH_TOKEN_002", "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_UNAUTHORIZED("AUTH_ACCESS_001", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    AUTH_FORBIDDEN("AUTH_ACCESS_002", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // ========== 사용자 관련 (USER_xxx) ==========
    USER_NOT_FOUND("USER_FIND_001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER_CREATE_001", "이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    USER_EMAIL_NOT_VERIFIED("USER_VERIFY_001", "이메일 인증이 필요합니다.", HttpStatus.FORBIDDEN),
    USER_INACTIVE("USER_STATUS_001", "비활성화된 계정입니다.", HttpStatus.FORBIDDEN),
    USER_DELETED("USER_STATUS_002", "삭제된 계정입니다.", HttpStatus.GONE),

    // ========== 프로젝트 관련 (PROJECT_xxx) ==========
    PROJECT_NOT_FOUND("PROJECT_FIND_001", "프로젝트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PROJECT_ACCESS_DENIED("PROJECT_ACCESS_001", "프로젝트 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    PROJECT_LIMIT_EXCEEDED("PROJECT_LIMIT_001", "프로젝트 생성 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),

    // ========== 컨테이너 관련 (CONTAINER_xxx) ==========
    CONTAINER_NOT_FOUND("CONTAINER_FIND_001", "컨테이너를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CONTAINER_ALREADY_RUNNING("CONTAINER_STATE_001", "이미 실행 중인 컨테이너입니다.", HttpStatus.CONFLICT),
    CONTAINER_START_FAILED("CONTAINER_OP_001", "컨테이너 시작에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ========== 저장소/파일 관련 (STORAGE_xxx) ==========
    STORAGE_LIMIT_EXCEEDED("STORAGE_LIMIT_001", "저장 공간이 부족합니다.", HttpStatus.INSUFFICIENT_STORAGE),
    FILE_NOT_FOUND("STORAGE_FILE_001", "파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED("STORAGE_FILE_002", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ========== 공통 에러 (COMMON_xxx) ==========
    COMMON_INVALID_PARAMETER("COMMON_PARAM_001", "잘못된 요청 파라미터입니다.", HttpStatus.BAD_REQUEST),
    COMMON_INTERNAL_ERROR("COMMON_SYS_001", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    COMMON_METHOD_NOT_ALLOWED("COMMON_HTTP_001", "지원하지 않는 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    COMMON_RESOURCE_NOT_FOUND("COMMON_HTTP_002", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    companion object {
        fun fromCode(code: String): ErrorCode? {
            return entries.find { it.code == code }
        }
    }
}