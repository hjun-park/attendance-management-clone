package com.hjun.timereport.member;

import static com.hjun.timereport.global.response.BaseResponseStatus.EMPTY_USER;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjun.timereport.global.constant.SessionConst;
import com.hjun.timereport.global.exception.BaseException;
import com.hjun.timereport.global.response.BaseResponseStatus;
import com.hjun.timereport.member.dto.MemberDto;
import com.hjun.timereport.member.dto.MemberLoginDto;
import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	public MemberServiceImpl(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public List<MemberDto> findMembers() {
		List<Member> members = memberRepository.findAll();

		return members.stream()
			.map(Member::entityToDto)
			.collect(Collectors.toList());
	}

	@Override
	public MemberDto findMemberDto(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(EMPTY_USER));

		return Member.entityToDto(member);
	}

	@Override
	public Member findMember(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> new BaseException(EMPTY_USER));
	}

	@Override
	public MemberDto login(MemberLoginDto memberLoginDto, HttpServletRequest request) {

		Member findMember = memberRepository.findByMemberCode(memberLoginDto.getMemberCode())
											.orElseThrow(() -> new BaseException(EMPTY_USER));

		if (!findMember.getPwd().equals(memberLoginDto.getPwd())) {
			throw new BaseException(BaseResponseStatus.CHECK_PASSWORD);
		}

		HttpSession session = request.getSession();
		session.setAttribute(SessionConst.LOGIN_MEMBER_ID, findMember.getMemberId());

		return Member.entityToDto(findMember);
	}

	@Override
	public String logout(HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		if (session == null)
			throw new BaseException(BaseResponseStatus.NOT_EXIST_SESSION);

		String memberId = session.getAttribute(SessionConst.LOGIN_MEMBER_ID).toString();

		session.invalidate();

		return memberId;

	}

	@Override
	public List<Member> findAllByDeptCode(String deptCode){
		return memberRepository.findAllByDeptCode(deptCode);
	}




}
