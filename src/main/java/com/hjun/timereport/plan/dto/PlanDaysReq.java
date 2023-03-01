package com.hjun.timereport.plan.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlanDaysReq {

	private List<String> planDays;

	public PlanDaysReq() {
	}


	@Builder
	public PlanDaysReq(List<String> planDays) {
		this.planDays = planDays;
	}
}
