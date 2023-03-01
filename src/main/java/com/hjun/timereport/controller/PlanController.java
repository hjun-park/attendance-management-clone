package com.hjun.timereport.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.hjun.timereport.global.constant.SessionConst;
import com.hjun.timereport.global.response.BaseResponse;
import com.hjun.timereport.plan.PlanService;
import com.hjun.timereport.plan.dto.PlanDaysReq;
import com.hjun.timereport.plan.dto.PlanDeleteReq;
import com.hjun.timereport.plan.dto.PlanDto;
import com.hjun.timereport.plan.dto.PlanSaveReq;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

	public PlanController(PlanService planService) {
        this.planService = planService;
    }


    @GetMapping
    public BaseResponse<List<PlanDto>> findPlans(
    		@RequestParam(defaultValue = "") String day,
    		@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
        return new BaseResponse<>(planService.findPlans(day, memberId));
    }

    @GetMapping("/detail")
    public BaseResponse<List<PlanDto>> findPlanDetails(@RequestBody PlanDaysReq planDaysReq,
    		@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
        return new BaseResponse<>(planService.findPlanDetails(planDaysReq, memberId));
    }

    //임시저장(Only 계획) (2주치 모두 등록) - 상태 변경 필요 X
    @PostMapping("/drafts")
    public BaseResponse<Long> draftPlan(@RequestBody List<PlanSaveReq> planSaveReqs,
    		@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
        return new BaseResponse<>(planService.draftPlan(planSaveReqs, memberId));
    }

    //선택 변경 (오늘 날짜 포함해서 이후 것들)
    @PatchMapping
    public BaseResponse<Long> editPlan(@RequestBody List<PlanSaveReq> planSaveReqs,
    		@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
        return new BaseResponse<>(planService.editPlan(planSaveReqs, memberId));
    }

    @PostMapping("/delete")
    public BaseResponse<Long> deletePlan(
    		@RequestParam(defaultValue = "") String day,
    		@RequestBody PlanDeleteReq planDeleteReq,
    		@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
    	return new BaseResponse<>(planService.deletePlans(planDeleteReq.getPlanIds(), memberId, day));
    }


}
