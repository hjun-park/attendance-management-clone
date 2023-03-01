package com.hjun.timereport.plan.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanDeleteReq {

	private List<Long> planIds;

	@Builder
	public PlanDeleteReq(List<Long> planIds) {
		this.planIds = planIds;
	}


}
