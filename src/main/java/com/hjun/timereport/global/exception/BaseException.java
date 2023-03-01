package com.hjun.timereport.global.exception;

import com.hjun.timereport.global.response.BaseResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
	BaseResponseStatus status;
}
