package com.yeahpeu.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    NOT_FOUND_USER_ID("NOT_FOUND", "유저가 존재하지 않습니다."),
    NOT_FOUND_OPPONENT("NOT_FOUND", "상대방이 존재하지 않습니다."),
    INVALID_PASSWORD("INVALID_PASSWORD", "비밀번호가 일치하지 않습니다."),

    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),

    NOT_FOUND_IMAGE_ID("NOT_FOUND", "요청하신 이미지는 존재하지 않습니다."),
    TOKEN_INVALID("TOKEN_ERROR", "로그인에 실패했습니다. 다시 로그인 해주세요."),
    LOGIN_REQUIRED("LOGIN_REQUIRED", "이 기능을 이용하려면 로그인이 필요합니다."),

    NOT_FOUND_WEDDING_ID("NOT_FOUND", "웨딩 계정이 존재하지 않습니다."),
    ALREADY_ONBOARD("REDUNDANT_ERROR", "이미 온보딩하였습니다."),
    ALREADY_JOINED("REDUNDANT_ERROR", "이미 조인하였습니다."),
    ALREADY_COUPLING("REDUNDANT_ERROR", "이미 두 명이 존재하는 웨딩입니다."),
    OCCUPIED_WEDDING_ROLE("OCCUPIED_WEDDING_ROLE", "웨딩 역할이 겹칩니다."),
    PARTNER_NOT_FOUND_MESSAGE("NOT_FOUND", "존재하지 않는 파트너입니다. 다시 입력해주세요"),

    NOT_FOUND_BUDGET("NOT_FOUND", "예산이 존재하지 않습니다. 온보딩을 해주세요!"),
    NOT_FOUND_EVENT_ID("NOT_FOUND", "해당하는 일정이 존재하지 않습니다."),
    NOT_FOUND_TASK_ID("NOT_FOUND", "해당하는 확인 목록이 존재하지 않습니다."),

    INVALID_DATE_RANGE("INVALID_DATE_RANGE", "요청한 기간이 유효하지 않습니다."),
    INVALID_DATE_FORMAT("INVALID_DATE_FORMAT", "date 형식이 올바르지 않습니다. (예: yyyy-MM-dd)"),
    INVALID_SIZE("INVALID_SIZE", "size 값이 유효하지 않습니다."),
    INVALID_AMOUNT("INVALID_AMOUNT", "금액이 유효하지 않습니다.(0 ~ 100,000,000)"),

    NOT_FOUND_MAIN_CATEGORY_ID("NOT_FOUND", "메인 카테고리가 존재하지 않습니다."),
    NOT_FOUND_SUBCATEGORY_ID("NOT_FOUND", "서브카테고리가 존재하지 않습니다."),
    NOT_MATCHED_CATEGORY("REDUNDANT_ERROR", "해당해는 상위 카테고리가 존재하지 않습니다."),

    NULL_IMAGE("IMAGE_ERROR", "업로드한 이미지 파일이 NULL입니다."),
    NOT_FOUND_CATEGORY_ID("NOT_FOUND", "카테고리가 존재하지 않습니다."),

    NOT_FOUND_WISHLIST_ID("NOT_FOUND", "위시리스트가 존재하지 않습니다."),
    NOT_FOUND_WISHITEM_ID("NOT_FOUND", "위시아이템이 존재하지 않습니다."),
    DUPLICATE_WISHITEM("REDUNDANT_ERROR", "이미 존재하는 위시 아이템입니다."),

    INVALID_IMAGE_URL("IMAGE_ERROR", "요청한 이미지 URL의 형식이 잘못되었습니다."),
    INVALID_IMAGE_PATH("IMAGE_ERROR", "이미지를 저장할 경로가 올바르지 않습니다."),
    FAIL_IMAGE_NAME_HASH("IMAGE_ERROR", "이미지 이름을 해싱하는 데 실패했습니다."),
    INVALID_IMAGE("IMAGE_ERROR", "올바르지 않은 이미지 파일입니다."),

    KEYWORD_NULL("KEYWORD_ERROR", "키워드를 입력해주세요."),
    FAIL_KEYWORD_ENCODE("ENCODE_ERROR", "키워드 인코딩에 실패했습니다."),

    FAIL_API_CALL("EXTERNAL_API_ERROR", "네이버 쇼핑 API에서 데이터를 가져오는 중 오류가 발생했습니다."),
    INVALID_API_URL("API_URL_ERROR", "요청한 API URL의 형식이 잘못되었습니다."),

    FAIL_EMAIL_SENDING("EMAIL_SEND_ERROR", "인증 코드 발송에 실패했습니다."),
    EMAIL_AUTH_CODE_ALREADY_SENT("DUPLICATE_EMAIL", "이미 인증번호가 전송된 이메일입니다."),
    NOT_FOUND_EMAIL_AUTH_INFO("NOT_FOUND", "발급된 인증코드가 없습니다. 이메일 인증을 다시 시도해주세요."),
    EMAIL_AUTH_EXPIRED("TIME_EXPIRED", "이메일 인증 시간이 만료되었습니다. 이메일 인증을 다시 시도해주세요."),
    FAIL_EMAIL_AUTH("AUTH_CODE_ERROR", "인증코드가 유효하지 않습니다."),
    ALREADY_VALIDATED_EMAIL("REDUNDANT_ERROR", "이미 인증된 이메일입니다."),
    EMAIL_NOT_VERIFIED("NOT_VERIFIED_ERROR", "인증이 완료되지 않은 이메일입니다.");

    private final String code;
    private final String message;
}
