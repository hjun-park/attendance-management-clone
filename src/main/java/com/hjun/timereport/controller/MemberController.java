package com.hjun.timereport.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hjun.timereport.global.auth.Authority;
import com.hjun.timereport.global.constant.Grade;
import com.hjun.timereport.global.response.BaseResponse;
import com.hjun.timereport.member.MemberService;
import com.hjun.timereport.member.dto.MemberDto;
import com.hjun.timereport.member.dto.MemberLoginDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@Authority(target = Grade.ADMIN)
	@GetMapping
	public BaseResponse<List<MemberDto>> findMembers() {
		return new BaseResponse<>(memberService.findMembers());
	}

	@PostMapping("/login")
	public BaseResponse<MemberDto> memberLogin(@Valid @RequestBody MemberLoginDto memberLoginDto,
		HttpServletRequest request) {
		return new BaseResponse<>(memberService.login(memberLoginDto, request));
	}

	@PostMapping("/logout")
	public BaseResponse<String> memberLogout(HttpServletRequest request) {

		return new BaseResponse<>(memberService.logout(request));
	}
}
