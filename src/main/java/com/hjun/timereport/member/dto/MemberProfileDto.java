package com.hjun.timereport.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileDto {

	private String pwd;
	private String phoneNumber;

	@Builder
	public MemberProfileDto(String pwd, String phoneNumber) {
		this.pwd = pwd;
		this.phoneNumber = phoneNumber;
	}

}
