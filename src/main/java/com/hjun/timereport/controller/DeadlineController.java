package com.hjun.timereport.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hjun.timereport.deadline.DeadlineService;
import com.hjun.timereport.deadline.dto.DeadlineDto;
import com.hjun.timereport.deadline.dto.DeadlineRegisterDto;
import com.hjun.timereport.deadline.dto.DeadlineReq;
import com.hjun.timereport.global.auth.Authority;
import com.hjun.timereport.global.constant.Grade;
import com.hjun.timereport.global.response.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/deadline")
public class DeadlineController {

	private final DeadlineService deadlineService;

	public DeadlineController(DeadlineService deadlineService) {
		this.deadlineService = deadlineService;
	}

	// 마감 월조회
	@GetMapping
	@Authority(target = Grade.ADMIN)
	public BaseResponse<List<DeadlineDto>> findDeadline(@RequestParam(defaultValue = "") String year,
			@RequestParam(defaultValue = "") String month) {
		return new BaseResponse<>(deadlineService.findDeadline(year, month));
	}

	// 월 마감처리
	@PatchMapping
	@Authority(target = Grade.ADMIN)
	public BaseResponse<DeadlineRegisterDto> registerDeadline(@RequestBody DeadlineReq deadlineReq) {
		return new BaseResponse<>(deadlineService.registerDeadline(deadlineReq));
	}

}
