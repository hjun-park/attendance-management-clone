package com.hjun.timereport.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.hjun.timereport.performance.PerformanceService;
import com.hjun.timereport.performance.dto.PerformanceDeleteReq;
import com.hjun.timereport.performance.dto.PerformanceDto;
import com.hjun.timereport.performance.dto.PerformanceNoEnterDto;
import com.hjun.timereport.performance.dto.PerformanceSaveEditReq;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/performances")
public class PerformanceController {

	private final PerformanceService performanceService;

	public PerformanceController(PerformanceService performanceService) {
		this.performanceService = performanceService;
	}

	// 실적 전체조회 & 특정 주 실적조회
	@GetMapping
	public BaseResponse<List<PerformanceDto>> findPerformance(@RequestParam(defaultValue = "") String day,
			@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
		return new BaseResponse<>(performanceService.findPerformanceV2(day, memberId));
	}

	// 실적 수정 (수정(저장)과 확정을 다같이 할 수 있음)
	@PatchMapping("/edit")
	public BaseResponse<Long> editPerformance(@RequestParam(defaultValue = "") String day,
			@RequestBody List<PerformanceSaveEditReq> perfEditReqs,
			@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
		return new BaseResponse<>(performanceService.editPerformance(day, perfEditReqs, memberId));
	}

	// 실적 확정
	@PatchMapping("/confirm")
	public BaseResponse<Long> confirmPerformance(@RequestParam(defaultValue = "") String day,
			@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) throws Exception {
		return new BaseResponse<>(performanceService.confirmPerformance(day, memberId));
	}

	// 미입력자 전체조회
	@GetMapping("/noenters")
	public BaseResponse<List<PerformanceNoEnterDto>> findNoEnterPerformance(
			@RequestParam(defaultValue = "") String year, @RequestParam(defaultValue = "") String month) {
		return new BaseResponse<>(performanceService.findNoEnterPerformance(year, month));
	}

	@PostMapping("/delete")
    public BaseResponse<Long> deletePerformance(
    		@RequestParam(defaultValue = "") String day,
    		@RequestBody PerformanceDeleteReq performanceDeleteReq,
//    		@RequestBody List<Long> planIds,
    		@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId,
    		HttpServletRequest request) {

		log.info("#### start");
		log.info("=====> {}", request.getAttributeNames());

//		return new BaseResponse<>(performanceService.deletePerformances(planIds, memberId, day));
    	return new BaseResponse<>(performanceService.deletePerformances(performanceDeleteReq.getPerfIds(), memberId, day));
    }

}
