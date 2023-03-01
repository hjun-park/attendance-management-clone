package com.hjun.timereport.deadline.constant;

import lombok.Getter;

@Getter
public enum DeadlineStatus{
	TRUE("1"),
	FALSE("0")
	;

	DeadlineStatus(String value) {
		this.value = value;
	}

	private String value;

}
