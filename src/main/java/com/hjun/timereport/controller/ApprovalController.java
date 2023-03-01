package com.hjun.timereport.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.hjun.timereport.global.auth.Authority;
import com.hjun.timereport.global.constant.Grade;
import com.hjun.timereport.global.constant.SessionConst;
import com.hjun.timereport.global.response.BaseResponse;
import com.hjun.timereport.performance.ApprovalService;
import com.hjun.timereport.performance.dto.ApprovalDto;
import com.hjun.timereport.performance.dto.ApprovalReq;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/approvals")
@Validated
public class ApprovalController {

	private final ApprovalService approvalService;

	public ApprovalController(ApprovalService approvalService) {
		this.approvalService = approvalService;
	}

	// 날짜&팀원 승인 페이지 조회
	@GetMapping
	@Authority(target = Grade.MANAGER)
	public BaseResponse<List<ApprovalDto>> findApprovals(@RequestParam(defaultValue = "") String day,
															@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
		return new BaseResponse<>(approvalService.findApprovals(day, memberId));
	}

	// 일괄 승인
	@PatchMapping
	@Authority(target = Grade.MANAGER)
	public BaseResponse<List<Long>> approvalAny(@RequestBody @Valid ApprovalReq approvalReq,
												@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
		return new BaseResponse<>(approvalService.approvalAny(approvalReq, memberId));
	}

	// 일괄 승인취소
	@PatchMapping("/cancel")
	@Authority(target = Grade.MANAGER)
	public BaseResponse<List<Long>> cancelApprovalAny(@RequestBody @Valid ApprovalReq approvalReq,
													@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
		return new BaseResponse<>(approvalService.cancelApprovalAny(approvalReq, memberId));
	}

	// 일괄 반려
	@PatchMapping("/reset")
	@Authority(target = Grade.MANAGER)
	public BaseResponse<List<Long>> resetApprovalAny(@RequestBody @Valid ApprovalReq approvalReq,
													@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID) Long memberId) {
		return new BaseResponse<>(approvalService.resetApprovalAny(approvalReq, memberId));
	}


}
