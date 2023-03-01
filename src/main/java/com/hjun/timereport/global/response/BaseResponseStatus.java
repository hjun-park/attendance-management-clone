package com.hjun.timereport.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러 코드 관리
 */
@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {
	/**
	 * 1000 : 요청 성공
	 */
	SUCCESS(true, 1000, "요청에 성공하였습니다."),


	/**
	 * 2000 : Request 오류
	 */

	// Common : 2000~
	REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
	INVALID_USER(false, 2003, "권한이 없는 유저의 접근입니다."),
	CHECK_PASSWORD(false, 2004, "비밀번호가 일치하지 않습니다."),
	EMPTY_PARAMETER(false, 2007, "파라미터 값을 확인해주세요."),
	INVALID_VERIFY_CODE(false, 2008, "잘못된 인증코드입니다."),
	NOT_EXIST_SESSION(false, 2009, "로그인 상태가 아닙니다."),
	NOT_TO_DELETE(false, 2010, "삭제할 대상이 없습니다."),
	WRONG_ACCESS(false, 2011, "잘못된 접근입니다."),

	// 계획 관련: 2100~
	NOT_TO_DELETE_PLANS(false, 2100, "요청하신 계획을 삭제할 수 없습니다."),

	// 실적 관련: 2200~
	NOT_EXIST_PERFORMANCE(false, 2200, "요청한 실적이 존재하지 않습니다."),
	INVALID_EDIT_PERFORMANCE_USER(false, 2201, "수정 요청한 실적의 유저와 실제 수정하려는 대상의 유저가 다릅니다."),
	INVALID_EDIT_PERFORMANCE_DAY(false, 2202, "수정 요청한 실적의 날짜와 실제 수정하려는 대상의 날짜가 다릅니다."),
	NOT_EXIST_CONFIRM_DAY(false, 2203, "실적 승인 요청 날짜가 존재하지 않습니다."),
	DUPLICATE_SEQUENCE_PERFORMANCE(false, 2204, "요청한 실적에 중복된 순서가 존재합니다."),
	INVALID_PERFORMANCE_SEQUENCE(false, 2205, "요청하신 실적의 순서가 잘못되었습니다."),
	INVALID_PERFORMANCE_HOUR(false, 2206, "요청하신 실적의 시작시간과 종료시간이 잘못되었습니다."),


	// 승인 관련: 2300~

	// 마감 관련: 2400~


	// 프로젝트 관련: 2500~

    // Holiday, TwoWeeks 관련 : 2600 ~
    INSERT_ERROR_TWO_WEEKS(false, 2600, "주 시작일 데이터 추가에 실패하였습니다."),
    INVALID_DATE_FORMAT(false, 2601, "입력한 날짜가 형식에 맞지 않습니다."),

    // Task 관련 : 2700 ~
    NOT_EXIST_TASK(false, 2700, "해당하는 업무를 찾을 수 없습니다."),

	/**
	 * 3000 : Response 오류
	 */
	// Common : 3000~
	RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
	RESPONSE_EMPTY(false, 3001, "조회할 정보가 없습니다."),
	DELETE_EMPTY(false, 3002, "삭제할 정보가 없습니다."),
	EMPTY_USER(false, 3003, "회원이 존재하지 않습니다."),

	// 계획 관련: 3100~

	// 실적 관련: 3200~
	EXIST_NO_CONFIRM_DAY(false, 3200, "확정되지 않은 이전 실적이 존재합니다."),
	ALREADY_APPROVAL_PERF(false, 3201, "이미 승인된 실적입니다."),

	// 승인 관련: 3300~

	// 마감 관련: 3400~
	EXIST_NO_APPROVAL_OF_PERF(false, 3400, "승인되지 않은 실적이 있어 마감을 할 수가 없습니다."),


	// 프로젝트 관련: 3500~
	NOT_EXIST_PROJECT(false, 3500, "존재하지 않은 프로젝트입니다."),
	DUPLICATE_PROJECT(false, 3501, "이미 추가된 프로젝트입니다."),
	EXIST_PROJECT_WITH_MEMBER(false, 3502, "해당 프로젝트를 추가한 회원이 존재합니다."),

	// 날짜 관련: 3600~
	NOT_EXIST_HOLIDAY(false, 3600, "휴일 정보가 존재하지 않습니다."),
	NOT_EXIST_TWO_WEEKS(false, 3601, "2주 정보가 존재하지 않습니다."),
	CHECK_THE_DATE(false, 3602, "날짜를 확인해주세요"),


	/**
	 * 4000 : Database, Server 오류
	 */
	DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
	SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
	SMS_ERROR(false, 4002, "SMS 인증 요청에 실패하였습니다."),
	ENCRYPTION_ERROR(false, 4011, "암호화에 실패하였습니다."),
	DECRYPTION_ERROR(false, 4012, "복호화에 실패하였습니다."),
	S3_UPLOAD_ERROR(false, 4021, "이미지 업로드에 실패하였습니다."),
	S3_FORMAT_ERROR(false, 4022, "이미지 형식이 올바르지 않습니다."),
	S3_DELETE_ERROR(false, 4023, "이미지 삭제에 실패하였습니다."),
	SEND_MAIL_ERROR(false, 4024, "이메일 전송에 실패하였습니다."),
	AES256_INVALID_SPEC(false, 4025, "AES256 초기화에 오류가 있습니다."),
	AES256_NO_SUCH_SPEC(false, 4026, "지정한 AES256 스펙을 찾을 수 없습니다."),

	/**
	 * 5000 : Bean Validation 및 기타
	 */
	VALID_INPUT_BLANK(false, 5000, "요청 중에 빈 값이 존재합니다."),
	VALID_NUMBER_FORMAT(false, 5001, "숫자 입력값에 문자가 존재합니다."),
	ADVICE_BEAN_VALIDATION(false, 5002, "Bean Validation 제약 조건에 위배됩니다."),
	ADVICE_REQ_BODY_BINDING(false, 5003, "요청값 확인이 필요합니다."),
	ADVICE_INVALID_URI_PARAM(false, 5004, "URI 파라미터 값 확인이 필요합니다."),
	ADVICE_INVALID_URI_PATH(false, 5005, "URI 경로가 잘못되었습니다."),
	ADVICE_NO_SESSION(false, 5006, "로그인 상태가 아닙니다."),
	VALID_POSITIVE(false, 5007, "숫자 형식이 아닙니다."),
	VALID_DATE_FORMAT(false, 5008, "올바른 날짜 형식이 아닙니다.");



	private final boolean isSuccess;
	private final int code;
	private final String message;

}
