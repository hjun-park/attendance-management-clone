package com.hjun.timereport.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginDto {

	@NotBlank
	private String memberCode;

	@NotBlank
	private String pwd;

	@Builder
	public MemberLoginDto(String memberCode, String pwd) {
		this.memberCode = memberCode;
		this.pwd = pwd;
	}
}
