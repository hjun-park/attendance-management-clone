package com.hjun.timereport.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hjun.timereport.member.dto.MemberDto;
import com.hjun.timereport.member.dto.MemberLoginDto;
import com.hjun.timereport.member.entity.Member;

public interface MemberService {

	List<MemberDto> findMembers();

	MemberDto login(MemberLoginDto memberLoginDto, HttpServletRequest request);

	String logout(HttpServletRequest request);

	MemberDto findMemberDto(Long memberId);

	Member findMember(Long memberId);

	List<Member> findAllByDeptCode(String deptCode);
}
