package com.hjun.timereport.global.exception;

import static com.hjun.timereport.global.response.BaseResponseStatus.ADVICE_BEAN_VALIDATION;
import static com.hjun.timereport.global.response.BaseResponseStatus.ADVICE_INVALID_URI_PARAM;
import static com.hjun.timereport.global.response.BaseResponseStatus.ADVICE_INVALID_URI_PATH;
import static com.hjun.timereport.global.response.BaseResponseStatus.ADVICE_NO_SESSION;
import static com.hjun.timereport.global.response.BaseResponseStatus.ADVICE_REQ_BODY_BINDING;
import static com.hjun.timereport.global.response.BaseResponseStatus.SERVER_ERROR;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.hjun.timereport.global.response.BaseResponse;
import com.hjun.timereport.global.response.BaseResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = "com.hjun.timereport")
public class BaseControllerAdvice {

	// 500번대 에러처리
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BaseResponse<BaseResponseStatus> unknownErrorHandler(Throwable throwable) {
		log.info("{}", throwable.getMessage());
		log.info("{}", throwable.getStackTrace().toString());
		return new BaseResponse<>(SERVER_ERROR);
	}

	// Bean Validation
	@ExceptionHandler(value = {ConstraintViolationException.class})
	public BaseResponse<BaseResponseStatus> errorBeanValidationHandler(ConstraintViolationException cve) {

		log.info(cve.getMessage());
		return new BaseResponse<>(ADVICE_BEAN_VALIDATION);
	}

	// @Requestbody binding error
	@ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
	public BaseResponse<BaseResponseStatus> errorRequestBodyBindingHandler(MethodArgumentTypeMismatchException mat) {

		log.info(mat.getMessage() + "mat");
		return new BaseResponse<>(ADVICE_REQ_BODY_BINDING);
	}

	// @RequestParam error
	@ExceptionHandler(value = {MissingServletRequestParameterException.class})
	public BaseResponse<BaseResponseStatus> errorRequestParamBindingHandler(
		MissingServletRequestParameterException msrpe) {

		log.info(msrpe.getMessage() + "missing");
		return new BaseResponse<>(ADVICE_INVALID_URI_PARAM);

	}

	// Servlet 상에서 발생하는 오류 (url 잘못 입력 or 인터셉터 상 오류)
	@ExceptionHandler(value = {ServletRequestBindingException.class})
	public BaseResponse<BaseResponseStatus> errorServletExceptionHandler(ServletRequestBindingException srbe,
		HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		log.info(srbe.getMessage() + "srbe");

		if (session == null)
			return new BaseResponse<>(ADVICE_NO_SESSION);

		return new BaseResponse<>(ADVICE_INVALID_URI_PATH);
	}

	// Bean validation Biding error
	@ExceptionHandler(value = {BindException.class})
	public BaseResponse<BaseResponseStatus> errorBaseResponeExceptionHandler(BindException be) {

		BindingResult bindingResult = be.getBindingResult();

		String bindCode = Objects.requireNonNull(bindingResult.getFieldError()).getCode();

		switch (bindCode) {
			case "NotBlank":
				return new BaseResponse<>(BaseResponseStatus.VALID_INPUT_BLANK);

			case "NumberFormat":
				return new BaseResponse<>(BaseResponseStatus.VALID_NUMBER_FORMAT);

			case "Positive":
				return new BaseResponse<>(BaseResponseStatus.VALID_POSITIVE);

			case "DateTimeFormat":
				return new BaseResponse<>(BaseResponseStatus.VALID_DATE_FORMAT);
		}

		return new BaseResponse<>(ADVICE_INVALID_URI_PATH);
	}

	// Custom response Error
	@ExceptionHandler(value = {BaseException.class})
	protected BaseResponse<BaseResponseStatus> handleCustomException(BaseException e) {
		return new BaseResponse<>(e.status);
	}

}
