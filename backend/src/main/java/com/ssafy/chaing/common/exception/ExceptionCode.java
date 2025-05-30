package com.ssafy.chaing.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    // User 관련
    USER_NOT_FOUND("USER_NOT_FOUND", "유저가 존재하지 않습니다."),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "이미 존재하는 이메일입니다."),

    // Group 관련
    DUPLICATE_NICKNAME("DUPLICATE_NICKNAME", "그룹 내에 이미 존재하는 닉네임입니다."),
    DUPLICATE_PROFILE_IMAGE("DUPLICATE_PROFILE_IMAGE", "그룹 내에 이미 존재하는 프로필 이미지입니다."),
    GROUP_FULL("GROUP_FULL", "그룹의 인원이 가득 찼습니다."),
    GROUP_NOT_FOUND("GROUP_NOT_FOUND", "그룹이 존재하지 않습니다."),
    GROUP_INVITE_CODE_INVALID("GROUP_INVITE_CODE_INVALID", "유효하지 않은 그룹 초대 코드입니다."),

    // Contract 관련
    AlREADY_CONFIRMED_CONTRACT("AlREADY_CONFIRMED_CONTRACT", "이미 확정된 계약서 입니다."),
    AlREADY_CONFIRMED_USER("AlREADY_CONFIRMED_USER", "이미 승인한 계약서 입니다."),
    CONTRACT_ALREADY_CONFIRMED("CONTRACT_ALREADY_CONFIRMED", "수정이 불가능합니다. 계약서가 이미 확정 되었습니다."),
    CONTRACT_ALREADY_EXIST("CONTRACT_ALREADY_EXIST", "이미 계약서가 존재합니다."),
    CONTRACT_NOT_FOUND("CONTRACT_NOT_FOUND", "계약서가 존재하지 않습니다."),
    CARD_NOT_FOUND("CARD_NOT_FOUND", "카드가 존재하지 않습니다."),
    CARD_ALREADY_EXIST("CARD_ALREADY_EXIST", "이미 카드가 존재합니다."),
    CONTRACT_USER_NOT_FOUND("CONTRACT_USER_NOT_FOUND", "사용자의 계약 정보가 존재하지 않습니다."),

    // 인증/인가 관련
    INVALID_PASSWORD("INVALID_PASSWORD", "비밀번호가 틀렸습니다."),
    INVALID_TOKEN("EXPIRED_ACCESS_TOKEN", "로그인에 실패하였습니다."),
    SOCIAL_NOT_FOUND("SOCIAL_NOT_FOUND", "지원하지 않는 로그인 방식입니다."),

    // 납부일 관련
    INVALID_DUEDATE("INVALID_DUEDATE", "유효하지 않은 납부일입니다. 납부일은 2일 이상 28일 이하여야 합니다."),

    // 그룹 멤버십 / 당번 관련
    DUTY_NOT_FOUND("DUTY_NOT_FOUND", "당번이 존재하지 않습니다."),
    USER_NOT_IN_GROUP("USER_NOT_IN_GROUP", "해당 유저가 그룹에 속해 있지 않습니다."),

    // 결제 관련
    USER_PAYMENT_NOT_FOUND("USER_PAYMENT_NOT_FOUND", "결제 정보에 해당하는 사용자가 없습니다."),
    LIVING_ACCOUNT_ALREADY_EXIST("LIVING_ACCOUNT_ALREADY_EXIST", "이미 계좌가 존재합니다."),
    RENT_ACCOUNT_ALREADY_EXIST("RENT_ACCOUNT_ALREADY_EXIST", "공과금/월세 계좌번호가 존재하지 않습니다."),

    // 날짜/시간 검증 관련
    INVALID_YEAR("INVALID_YEAR", "입력된 '년도'가 범위를 초과하였습니다."),
    INVALID_MONTH("INVALID_MONTH", "입력된 '월'이 범위를 초과하였습니다."),

    // 생활 룰 관련
    LIFE_RULE_NOT_FOUND("LIFE_RULE_NOT_FOUND", "생활 룰이 존재하지 않습니다."),
    LIFE_RULE_ITEM_NOT_FOUND("LIFE_RULE_ITEM_NOT_FOUND", "생활 룰 변경 사항이 존재하지 않습니다."),
    LIFE_RULE_CHANGE_REQUEST_NOT_FOUND("LIFE_RULE_CHANGE_REQUEST_NOT_FOUND", "변경 요청이 존재하지 않습니다."),
    LIFE_RULE_ALREADY_EXISTS("LIFE_RULE_ALREADY_EXISTS", "이미 생성된 생활 수칙이 존재합니다."),
    LIFE_RULE_USER_NOT_FOUND("LIFE_RULE_USER_NOT_FOUND", "해당 생활 룰 사용자 정보가 존재하지 않습니다."),
    LIFE_RULE_USER_ALREADY_VOTED("LIFE_RULE_USER_ALREADY_VOTED", "이미 승인/거절한 사용자는 다시 처리할 수 없습니다."),
    INVALID_RULE_ACTION_TYPE("INVALID_RULE_ACTION_TYPE", "유효하지 않은 생활 룰 변경 유형입니다."),
    LIFE_RULE_CHANGE_ALREADY_IN_PROGRESS("LIFE_RULE_CHANGE_ALREADY_IN_PROGRESS", "이미 진행 중인 생활 룰 변경 요청이 존재합니다."),


    // S3 관련
    S3_UPLOAD_FAILED("S3_UPLOAD_FAILED", "파일 업로드에 실패하였습니다."),
    S3_DELETE_FAILED("S3_DELETE_FAILED", "파일 삭제에 실패하였습니다."),

    // 금융/기타 관련
    CONTRACT_TRANSACTION_REGISTRATION_FAILED("CONTRACT_TRANSACTION_REGISTRATION_FAILED",
            "서약서 스마트 컨트랙트 등록 중 오류가 발생하였습니다."),
    CONTRACT_TRANSACTION_RETRIEVE_FAILED("CONTRACT_TRANSACTION_RETRIEVE_FAILED", "서약서 스마트 컨트랙트 조회 중 오류가 발생하였습니다."),
    LIVE_ACCOUNT_REGISTRATION_FAILED("LIVE_ACCOUNT_REGISTRATION_FAILED", "스마트 컨트랙트에 생활비 계좌 등록 중 오류가 발생하였습니다."),
    LIVE_ACCOUNT_RETRIEVE_FAILED("LIVE_ACCOUNT_RETRIEVE_FAILED", "스마트 컨트랙트에 생활비 계좌 조회 중 오류가 발생하였습니다."),
    TRANSFER_TRANSACTION_REGISTRATION_FAILED("TRANSFER_TRANSACTION_REGISTRATION_FAILED",
            "이체 내역 스마트 컨트랙트 등록 중 오류가 발생하였습니다."),
    TRANSFER_TRANSACTION_RETRIEVE_FAILED("TRANSFER_TRANSACTION_RETRIEVE_FAILED", "이체 내역 스마트 컨트랙트 조회 중 오류가 발생하였습니다."),

    PDF_GENERATION_FAILED("PDF_GENERATION_FAILED", "PDF 생성 중 오류가 발생하였습니다."),
    PDF_IS_GENERATING("PDF_IS_GENERATING", "PDF가 생성 중입니다."),

    TRANSFER_PORTFOLIO_IS_NULL("TRANSFER_PORTFOLIO_IS_NULL", "이체에 대한 정보가 비어있습니다."),
    FINTECH_TRANSFER_FAILED("FINTECH_TRANSFER_FAILED", "핀테크 송금에 실패하였습니다."),

    GPT_REQUEST_FAILED("GPT_REQUEST_FAILED", "GPT 응답 처리 중 오류가 발생했습니다."),

    PAY_NOT_COLLECTED("PAY_NOT_COLLECTED", "아직 금액이 모아지지 않았습니다."),
    ALREADY_PAID("ALREADY_PAID", "이미 지불한 내역입니다.");

    private final String code;
    private final String message;
}


