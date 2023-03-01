package com.hjun.timereport.performance.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceDeleteReq {

	private List<Long> perfIds;

	public PerformanceDeleteReq(List<Long> perfIds) {
		this.perfIds = perfIds;
	}

}
