package com.hjun.timereport.plan;

import java.util.List;

import com.hjun.timereport.plan.dto.PlanDaysReq;
import com.hjun.timereport.plan.dto.PlanDto;
import com.hjun.timereport.plan.dto.PlanSaveReq;

public interface PlanService {

	// 계획 전체 조회 (메인페이지)
	List<PlanDto> findPlans(String day, Long memberId);

	// 선택 계획 조회
	List<PlanDto> findPlanDetails(PlanDaysReq planDaysReq, Long memberId);

	// 선택 계획 임시 저장 (계획에만 저장) - 오늘날짜 포함 이후
	Long draftPlan(List<PlanSaveReq> planSaveReq, Long memberId);

	// 선택 계획 수정 등록 (실적에도 저장, 계획에는 수정 (등록기능))
	Long editPlan(List<PlanSaveReq> planSaveReq, Long memberId);

	boolean isExistPlanDayBetweenBy(String startDay, String endDay, Long memberId);

	Long deletePlans(List<Long> planIds, Long memberId, String day);

	Long draftPlanOld(List<PlanSaveReq> planSaveReqs, Long memberId);
}
