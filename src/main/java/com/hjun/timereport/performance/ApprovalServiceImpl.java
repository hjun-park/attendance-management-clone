package com.hjun.timereport.performance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjun.timereport.biweekly.BiWeeklyService;
import com.hjun.timereport.biweekly.entity.Holiday;
import com.hjun.timereport.deadline.DeadlineService;
import com.hjun.timereport.deadline.entity.Deadline;
import com.hjun.timereport.member.MemberService;
import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.constant.PerformanceStatus;
import com.hjun.timereport.performance.dto.ApprovalDto;
import com.hjun.timereport.performance.dto.ApprovalReq;
import com.hjun.timereport.performance.entity.Performance;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ApprovalServiceImpl implements ApprovalService {

	private final MemberService memberService;
	private final PerformanceService performanceService;
	private final BiWeeklyService biWeeklyService;
	private final DeadlineService deadlineService;

	public ApprovalServiceImpl(
			MemberService memberService,
			PerformanceService performanceService,
			BiWeeklyService biWeeklyService,
			DeadlineService deadlineService
			) {
		this.memberService = memberService;
		this.performanceService = performanceService;
		this.biWeeklyService = biWeeklyService;
		this.deadlineService = deadlineService;
	}
	// 날짜&팀원 승인 페이지 조회
	@Override
	public List<ApprovalDto> findApprovals(String day, Long memberId) {

		Member currentMember = memberService.findMember(memberId);

		String deptCode = currentMember.getDeptCode();

		List<Member> memberList = memberService.findAllByDeptCode(deptCode);

		LocalDate today =LocalDate.now();
		if(day !=null && !day.isBlank()) {
			biWeeklyService.isValidDate(day);
			today = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyyMMdd"));
		}

		LocalDate mon = today.with(DayOfWeek.MONDAY);
		LocalDate sun = today.with(DayOfWeek.SUNDAY);
		String startDay = mon.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String endDay = sun.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		//실적있는날들의 실적 리스트
		List<Performance> perfDayList = performanceService.findByPerfDayBetweenAndDeptCodeAndSeq(startDay, endDay, deptCode);
		List<ApprovalDto> resultList = new ArrayList<>();

		//모든 날짜 리스트
		List<String> monToSunDateList = mon.datesUntil(sun.plusDays(1))
				.map(date -> date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.collect(Collectors.toList());

		//해당주의 holiday 조회
		Map<String, String> holidaysMap = biWeeklyService.findHolidayDateBetween(startDay, endDay).stream()
				.collect(Collectors.toMap(Holiday::getHolidayDate, Holiday::getIsHoliday));

		//해당주의 deadline 조회
		Map<String, String> deadlineMap = deadlineService.findByDeadlineDayBetween(startDay, endDay).stream()
				.collect(Collectors.toMap(Deadline::getDeadlineDay, Deadline::getIsDeadline));
		// 1. 사원별 데이터 생성
		for(Member member : memberList) {

			List<Performance> memberPerfs = perfDayList.stream()
					.filter(perf -> perf.getMember().getMemberId() == member.getMemberId())
					.collect(Collectors.toList());

			// 2. 사원의 총 일한 시간
			Double totalWorkTime = totalWorkTime(memberPerfs);

			// 3. 각 사원마다 월~일요일까지 날자별 데이터 생성
			for(String dayOfWeek : monToSunDateList) {

				Optional<Performance> dayPerfOfMemberOpt = memberPerfs.stream()
						.filter(perf -> perf.getPerfDay().equals(dayOfWeek))
						.findAny();
				// 4-1. 휴일 일때
				if (!dayPerfOfMemberOpt.isPresent()) {
					String isHoliday = holidaysMap.get(dayOfWeek);
					String isDeadline = deadlineMap.get(dayOfWeek);
					ApprovalDto approvalDto = ApprovalDto.ofAtHoliday(dayOfWeek, member, totalWorkTime, isHoliday, isDeadline);
					resultList.add(approvalDto);
					continue;
				}
				// 4-2. 휴일 아닐때
				Performance dayPerfOfMember = dayPerfOfMemberOpt.get();
				String isHoliday = holidaysMap.get(dayOfWeek);
				String isDeadline = deadlineMap.get(dayOfWeek);
				ApprovalDto approvalDto = ApprovalDto.of(dayPerfOfMember, member, totalWorkTime, isHoliday, isDeadline);
				resultList.add(approvalDto);
			}
		}

		// 5. resultList 정렬(1. 날짜순 2. 멤버 아이디순)
		Collections.sort(resultList, (r1, r2) -> {
            int r1PerfDay = r1.getPerfDay();
            int r2PerfDay = r2.getPerfDay();
            if(r1PerfDay == r2PerfDay){
                return Long.compare(r1.getMemberId(),r2.getMemberId());
            }
            return Integer.compare(r1PerfDay,r2PerfDay);
        });

		return resultList;

	}

	// 일괄 승인
	@Transactional
	@Override
	public List<Long> approvalAny(ApprovalReq approvalReq, Long memberId) {

		List<Long> memberIds = approvalReq.getMemberIds();
		List<String> days = approvalReq.getDays();

		List<Performance> perfDayList = performanceService.perfDayList(approvalReq, memberId);

        List<Long> approvalMember = new ArrayList<>();

        for(Long eachMemberIds : memberIds) {

        	List<Performance> memberPerfs = perfDayList.stream()
					.filter(perf -> perf.getMember().getMemberId() == eachMemberIds)
					.collect(Collectors.toList());
        	Long eachApprovalMember = memberPerfs.get(0).getMember().getMemberId();
        	approvalMember.add(eachApprovalMember);

        	for(String dayOfWeek : days) {
        		memberPerfs.stream()
        				.filter(perf -> perf.getPerfDay().equals(dayOfWeek))
        				.collect(Collectors.toList())
        				.forEach(perf -> perf.updateSignStatus(PerformanceStatus.APPROVAL.getValue()));
        	}
        }

		return approvalMember;
	}

	// 일괄 승인취소
	@Transactional
	@Override
	public List<Long> cancelApprovalAny(ApprovalReq approvalReq, Long memberId) {

		List<Long> memberIds = approvalReq.getMemberIds();
		List<String> days = approvalReq.getDays();

		List<Performance> perfDayList = performanceService.perfDayList(approvalReq, memberId);

        List<Long> cancelMember = new ArrayList<>();

        for(Long eachMemberIds : memberIds) {

        	List<Performance> memberPerfs = perfDayList.stream()
					.filter(perf -> perf.getMember().getMemberId() == eachMemberIds)
					.collect(Collectors.toList());
        	Long eachCancelMember = memberPerfs.get(0).getMember().getMemberId();
        	cancelMember.add(eachCancelMember);

        	for(String dayOfWeek : days) {
        		memberPerfs.stream()
        				.filter(perf -> perf.getPerfDay().equals(dayOfWeek))
        				.collect(Collectors.toList())
        				.forEach(perf -> perf.updateSignStatus(PerformanceStatus.SAVE.getValue()));
        	}
        }

		return cancelMember;
	}

	// 일괄 반려
	@Transactional
	@Override
	public List<Long> resetApprovalAny(ApprovalReq approvalReq, Long memberId) {

		List<Long> memberIds = approvalReq.getMemberIds();
		List<String> days = approvalReq.getDays();

		List<Performance> perfDayList = performanceService.perfDayList(approvalReq, memberId);

        List<Long> resetMember = new ArrayList<>();

        for(Long eachMemberIds : memberIds) {

        	List<Performance> memberPerfs = perfDayList.stream()
					.filter(perf -> perf.getMember().getMemberId() == eachMemberIds)
					.collect(Collectors.toList());
        	Long eachResetMember = memberPerfs.get(0).getMember().getMemberId();
        	resetMember.add(eachResetMember);

        	for(String dayOfWeek : days) {
        		memberPerfs.stream()
        				.filter(perf -> perf.getPerfDay().equals(dayOfWeek))
        				.collect(Collectors.toList())
        				.forEach(perf -> perf.updateSignStatus(PerformanceStatus.SAVE.getValue()));
        	}
        }

		return resetMember;
	}

	// 회원의 총 일한 시간
	private Double totalWorkTime(List<Performance> memberPerfs) {
		Double totalWorkTime = 0.0;
		for(Performance memberPerf : memberPerfs) {
			totalWorkTime += memberPerf.getDayHour();
		}
		return totalWorkTime;
	}

}
