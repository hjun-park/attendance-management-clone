package com.hjun.timereport.performance.constant;

import lombok.Getter;

@Getter
public enum PerformanceStatus{
	SAVE("1"),
	CONFIRM("2"),
	APPROVAL("3")
	;

	PerformanceStatus(String value){
		this.value = value;
	}

	private String value;

}
