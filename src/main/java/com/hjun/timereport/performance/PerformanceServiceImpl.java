package com.hjun.timereport.performance;

import static com.hjun.timereport.global.response.BaseResponseStatus.NOT_EXIST_PERFORMANCE;
import static com.hjun.timereport.global.response.BaseResponseStatus.NOT_TO_DELETE_PLANS;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjun.timereport.biweekly.BiWeeklyService;
import com.hjun.timereport.biweekly.dto.HolidayDto;
import com.hjun.timereport.deadline.entity.Deadline;
import com.hjun.timereport.deadline.repository.DeadlineRepository;
import com.hjun.timereport.global.exception.BaseException;
import com.hjun.timereport.global.response.BaseResponseStatus;
import com.hjun.timereport.global.util.SlackUtil;
import com.hjun.timereport.member.MemberService;
import com.hjun.timereport.member.dto.MemberDto;
import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.dto.ApprovalReq;
import com.hjun.timereport.performance.dto.NoEnterPerfDayOfMemeberDto;
import com.hjun.timereport.performance.dto.PerformanceDayDto;
import com.hjun.timereport.performance.dto.PerformanceDto;
import com.hjun.timereport.performance.dto.PerformanceNoEnterDto;
import com.hjun.timereport.performance.dto.PerformanceProjectDto;
import com.hjun.timereport.performance.dto.PerformanceSaveEditReq;
import com.hjun.timereport.performance.dto.PerformanceTaskDto;
import com.hjun.timereport.performance.entity.Performance;
import com.hjun.timereport.performance.repository.PerformanceQueryRepository;
import com.hjun.timereport.performance.repository.PerformanceRepository;
import com.hjun.timereport.plan.repository.PlanQueryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PerformanceServiceImpl implements PerformanceService {

	private final PerformanceRepository performanceRepository;
	private final MemberService memberService;
	private final BiWeeklyService biWeeklyService;
	private final PerformanceQueryRepository performanceQueryRepository;
	private final PlanQueryRepository planQueryRepository;
	private final DeadlineRepository deadlineRepository;
	private final SlackUtil slackAlarm;

	public PerformanceServiceImpl(PerformanceRepository performanceRepository, MemberService memberService,
			BiWeeklyService biWeeklyService, PerformanceQueryRepository performanceQueryRepository,
			PlanQueryRepository planQueryRepository, DeadlineRepository deadlineRepository, SlackUtil slackAlarm) {
		this.performanceRepository = performanceRepository;
		this.memberService = memberService;
		this.biWeeklyService = biWeeklyService;
		this.performanceQueryRepository = performanceQueryRepository;
		this.planQueryRepository = planQueryRepository;
		this.deadlineRepository = deadlineRepository;
		this.slackAlarm = slackAlarm;
	}

	@Override
	public List<PerformanceDayDto> findPerformanceV1(String day, Long memberId) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String startDay = LocalDate.now().format(formatter);

		if (!day.isBlank()) {
			startDay = day;
		}

		log.info("before startday = ", startDay);
		startDay = biWeeklyService.findStartOfWeekday(startDay);
		log.info("after startday = ", startDay);


		String endDay = LocalDate.parse(startDay, formatter).plusDays(13).format(formatter);

		Member findMember = memberService.findMember(memberId);

		List<Performance> findPerformances = performanceRepository.findByMemberAndPerfDayBetweenOrderByPerfDay(
				findMember, startDay, endDay);

		Map<String, List<Performance>> groupByPerfDay = findPerformances.stream()
				.collect(Collectors.groupingBy(Performance::getPerfDay));

		Map<String, List<PerformanceProjectDto>> projectDtos = new HashMap<>();
		Map<String, List<PerformanceTaskDto>> taskDtos = new HashMap<>();

		// << 2-1 >>
		findPerformances.forEach(p -> {
			if (p.getCodeId().equals("0")) {
				PerformanceTaskDto performanceTaskDto = PerformanceTaskDto.builder()
						.perfId(p.getPerfId())
						.seq(p.getSeq())
						.taskHour(p.getTaskHour())
						.groupMainId(p.getGroupMainId())
						.groupSubId(p.getGroupSubId())
						.codeId(p.getCodeId())
						.codeMainNm(p.getCodeMainNm())
						.codeSubNm(p.getCodeSubNm())
						.workDetail(p.getWorkDetail())
						.build();

				taskDtos.computeIfAbsent(p.getPerfDay(), k -> new ArrayList<>()).add(performanceTaskDto);
			} else {
				PerformanceProjectDto performanceProjectDto = PerformanceProjectDto.builder()
						.perfId(p.getPerfId())
						.seq(p.getSeq())
						.taskHour(p.getTaskHour())
						.groupMainId(p.getGroupMainId())
						.groupSubId(p.getGroupSubId())
						.codeId(p.getCodeId())
						.codeMainNm(p.getCodeMainNm())
						.codeSubNm(p.getCodeSubNm())
						.workDetail(p.getWorkDetail())
						.build();

				projectDtos.computeIfAbsent(p.getPerfDay(), k -> new ArrayList<>()).add(performanceProjectDto);
			}
		});

		// 1-2. 주말/공휴일 가져오기
		List<String> holidayLists = biWeeklyService
				.findHolidayDateBetweenList(startDay, endDay).stream()
				.map(HolidayDto::getHolidayDate)
				.collect(Collectors.toList());

		// 2. 날짜 14일 순회하면서 DayDto 생성
		return Stream.iterate(LocalDate.parse(startDay, formatter), tempDay -> tempDay.plusDays(1)).limit(14)
				.filter(dates -> !holidayLists.contains(dates.format(formatter))).filter(dates -> !groupByPerfDay
						.getOrDefault(dates.format(formatter), Collections.emptyList()).isEmpty())
				.map(dates -> {
					String parsedDay = dates.format(formatter);
					Performance performance = groupByPerfDay.getOrDefault(parsedDay, Collections.emptyList()).get(0);

					return PerformanceDayDto.builder()
							.perfDay(performance.getPerfDay())
							.startedHour(performance.getStartedHour())
							.endedHour(performance.getEndedHour())
							.signStatus(performance.getSignStatus())
							.dayHour(performance.getDayHour())
							.breakTime(performance.getBreakTime())
							.overtimeDetail(performance.getOvertimeDetail())
							.wfhYn(performance.getWfhYn())
							.projectDtoList(projectDtos.getOrDefault(parsedDay, Collections.emptyList()))
							.taskDtoList(taskDtos.getOrDefault(parsedDay, Collections.emptyList())).build();
				}).collect(Collectors.toList());

	}

	@Override
	public List<PerformanceDto> findPerformanceV2(String day, Long memberId) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String startDay = LocalDate.now().format(formatter);

		if (!day.isBlank()) {
			startDay = day;
		}

		biWeeklyService.isValidDate(startDay);

		startDay = biWeeklyService.findStartOfWeekday(startDay);


		String endDay = LocalDate.parse(startDay, formatter).plusDays(13).format(formatter);

		Member findMember = memberService.findMember(memberId);

		List<PerformanceDto> findPerformances = performanceQueryRepository.findMemberAndPerfDay(findMember, startDay, endDay);


		Map<String, List<PerformanceDto>> findPerfMap = findPerformances.stream()
				.collect(Collectors.groupingBy(p -> p.getPerfDay().toString()));


		// 3. 휴일 테이블 통해서 휴일 가져오기
		Map<String, String> holidayMap = biWeeklyService.findHolidayBetweenMap(startDay, endDay);


		Stream.iterate(LocalDate.parse(startDay, formatter), t -> t.plusDays(1))
				.limit(14)
				.filter(t -> !findPerfMap.containsKey(t.format(formatter)))
				.forEach(t -> findPerformances.add(PerformanceDto.dummyDto(t.format(formatter), holidayMap.getOrDefault(t.format(formatter), "Y"))));



		return findPerformances.stream()
			.sorted(Comparator.comparing(PerformanceDto::getPerfDay))
			.collect(Collectors.toList());

	}

	@Override
	@Transactional
	public Long editPerformance(String day, List<PerformanceSaveEditReq> perfEditReqs, Long memberId) {

		biWeeklyService.isValidDate(day);

		Deadline deadline = deadlineRepository.findById(day).orElseThrow(() -> new BaseException(BaseResponseStatus.WRONG_ACCESS));

		// 3. [에러코드] 마감된 일정 수정하려고 함
		if (deadline.getIsDeadline().equals("1"))
			throw new BaseException(BaseResponseStatus.ALREADY_APPROVAL_PERF);

		// 3-1. 시간 합산
		double hours = perfEditReqs.stream()
			.mapToDouble(PerformanceSaveEditReq::getTaskHour)
			.sum();


		// 4. 해당 날짜, memberId에 속한 Performance
		Member member = memberService.findMember(memberId);
		List<Performance> findPerformances = performanceRepository.findByMemberAndPerfDay(member, day);

		checkIsValidSeqAndHour(perfEditReqs);

		//  5. [저장] stream, filter 돌면서 저장
		perfEditReqs.stream()
			.filter(per -> per.getPerfId() == 0L)
			.forEach(per -> {

				Performance performance = Performance.builder()
					.seq(per.getSeq())
					.taskHour(hours)
					.perfDay(day)
					.dayHour(per.getDayHour())
					.startedHour(per.getStartedHour())
					.endedHour(per.getEndedHour())
					.groupMainId(per.getGroupMainId())
					.groupSubId(per.getGroupSubId())
					.codeId(per.getCodeId())
					.codeMainNm(per.getCodeMainNm())
					.codeSubNm(per.getCodeSubNm())
					.workDetail(per.getWorkDetail())
					.wfhYn(per.getWfhYn())
					.signStatus(per.getSignStatus())
					.breakTime(per.getBreakTime())
					.overtimeDetail(per.getOvertimeDetail())
					.member(member)
					.build();

				performanceRepository.save(performance);
			});

		// 6. [수정] stream, filter 돌면서 수정
		Map<Long, PerformanceSaveEditReq> perfIdMap = perfEditReqs.stream()
				.filter(per -> per.getPerfId() != 0L)
				.collect(Collectors.toMap(PerformanceSaveEditReq::getPerfId, o -> o));

		findPerformances.forEach(perf -> {
			try {
				if(perfIdMap.containsKey(perf.getPerfId()))
					perf.editPerformance(perfIdMap.get(perf.getPerfId()));
			} catch (Exception e) {
				// TODO: 에러코드 변경
				throw new BaseException(BaseResponseStatus.WRONG_ACCESS);
			}
		});


		return memberId;

	}

	@Override
	@Transactional
	public Long confirmPerformance(String day, Long memberId) throws Exception {
		if (day.isBlank())
			throw new BaseException(BaseResponseStatus.NOT_EXIST_CONFIRM_DAY);

		biWeeklyService.isValidDate(day);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate today = LocalDate.parse(day, formatter);
		LocalDate mon = today.with(DayOfWeek.MONDAY);
		LocalDate fri = today.with(DayOfWeek.FRIDAY);

		// 전날 미확정 상태라면 확정 못 하도록
		// 1. (from 주 시작날짜 to 어제까지) 체크 ( 2가 아닌게 있으면 )
		// 2. 만약 확정 안 되어 있으면 throw
		String startOfWeekDay = biWeeklyService.findStartOfWeekday(day);

		String targetDay = today.format(formatter);

		List<Performance> performancesBetween = performanceRepository.findByPerfDayBetween(startOfWeekDay, targetDay);

		Optional<Integer> minSignStatus = performancesBetween.stream()
			.takeWhile(t -> Integer.parseInt(t.getPerfDay()) < Integer.parseInt(targetDay))
			.map(t -> Integer.parseInt(t.getSignStatus()))
			.reduce(Integer::min);

		if (minSignStatus.isPresent() && minSignStatus.get() < 2)
			throw new BaseException(BaseResponseStatus.EXIST_NO_CONFIRM_DAY);

		List<Performance> findPerformances = performanceRepository.findByPerfDayAndMemberMemberIdOrderBySignStatus(day, memberId);

		if (findPerformances.isEmpty())
			throw new BaseException(NOT_EXIST_PERFORMANCE);

		findPerformances.stream()
				.takeWhile(p -> Integer.parseInt(p.getSignStatus()) < 2)
				.forEach(Performance::setSignStatusToConfirm);

		Double perfTotalWorkHour = performanceQueryRepository.getWeeklyWorkHour(
												mon.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
												today.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
												memberId).orElse(0.0);

		Double planTotalWorkHour = planQueryRepository.getWeeklyWorkHour(
												today.plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")),
												fri.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
												memberId).orElse(0.0);

		Double weeklyWorkHour = perfTotalWorkHour + planTotalWorkHour;

		if(weeklyWorkHour > 50) {
			slackAlarm.sendSlack(weeklyWorkHour);
		}
		return memberId;
	}

	@Override
	public List<PerformanceNoEnterDto> findNoEnterPerformance(String year, String month) {

		if (year.isBlank() && month.isBlank()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
			LocalDate today = LocalDate.now();
			year = String.valueOf(today.getYear());
			month = String.valueOf(today.format(formatter));
		}
		biWeeklyService.isValidYearAndMonth(year, month);
		// 결과 ARRAY
		List<PerformanceNoEnterDto> resultList = new ArrayList<PerformanceNoEnterDto>();
		// 1. member 조회
		List<MemberDto> memberList = memberService.findMembers();
		// 2.DB에서 member_id랑 perf_day asc로 정렬을 미리 시킨상태로 가져옴 status=1
		List<String> signStatus = Arrays.asList("0", "1");
		List<Performance> performanceList = performanceRepository
				.findBySignStatusInAndPerfDayStartsWithOrderByMemberAscPerfDayAsc(signStatus, year + month);
		// 3. key Member고 데이터가 noPerfday면 끝나네
		Map<Long, String> memberOfPefDay = performanceList.stream()
				.map(p -> NoEnterPerfDayOfMemeberDto.builder()
						.memberId(p.getMember().getMemberId())
						.perfDay(p.getPerfDay()).build())
				.distinct()
				.collect(groupingBy(NoEnterPerfDayOfMemeberDto::getMemberId,
						mapping(NoEnterPerfDayOfMemeberDto::getPerfDay, joining(", "))));
		for (MemberDto memberItem : memberList) {
			resultList.add(PerformanceNoEnterDto.converToDto(memberItem, memberOfPefDay.get(memberItem.getMemberId())));
		}
		return resultList;
	}


	@Override
	@Transactional
	public Long savePerformancesFromPlan(List<PerformanceDto> performances, Long memberId) {
		List<Performance> performanceList = performances.stream()
			.map(pd -> {
				Member member = memberService.findMember(pd.getMemberId());
				return Performance.dtoToEntity(pd, member);
			}).collect(Collectors.toList());

		Map<String, List<Performance>> perfDayMap = performanceList.stream()
				.collect(Collectors.groupingBy(Performance::getPerfDay));

		Set<String> days = perfDayMap.keySet();

		days.forEach(day -> {
			performanceRepository.deleteByPerfDay(day);
		});


		performanceRepository.saveAll(performanceList);
		return memberId;
	}

	@Override
	public boolean findPerformanceWithProject(String projectCode) {
		return performanceRepository.existsByGroupMainId(projectCode);
	}

	// 승인 조회시
	@Override
	public List<Performance> findByPerfDayBetweenAndDeptCodeAndSeq(String startDate, String endDate, String deptCode){
		return performanceRepository.findByPerfDayBetweenAndDeptCodeAndSeq(startDate, endDate, deptCode);

	}

	//승인 상태 변경시
	@Override
	public List<Performance> findByPerfDayBetweenAndMemberDeptCode(String startDate, String endDate, String deptCode){
		return performanceRepository.findByPerfDayBetweenAndMemberDeptCode(startDate, endDate, deptCode);

	}

	// 마감시 해당 월 승인 여부 조회
	@Override
	public boolean findPerformanceSignStatusExists (String day){
		return performanceQueryRepository.findPerformanceSignStatusExists(day);
	}

	// 승인 상태 변경시 공통 코드
	@Override
	public List<Performance> perfDayList(ApprovalReq approvalReq, Long memberId){
		Member currentMember = memberService.findMember(memberId);

		String deptCode = currentMember.getDeptCode();

		String startDate = approvalReq.getStartDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate weekStartDate = LocalDate.parse(startDate, formatter);
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        // 해당 주 사원들의 실적 리스트
        List<Performance> perfDays = performanceRepository.findByPerfDayBetweenAndMemberDeptCode(
        		weekStartDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        		, weekEndDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        		, deptCode);

        return perfDays;
	}

    @Override
    public boolean isExistPerfDayBetweenBy(String startDay, String endDay, Long memberId) {
            return performanceRepository.existsByMemberMemberIdAndPerfDayBetween(memberId, startDay, endDay);
    }

	@Override
	@Transactional
	public Long deletePerformances(List<Long> perfIds, Long memberId, String day) {

		List<Performance> performances = performanceRepository.findByPerfDayAndMemberMemberId(day, memberId);
		List<Long> findPerformanceIds = performances.stream().map(Performance::getPerfId).collect(Collectors.toList());

		// 검증
		perfIds.forEach(perfId -> {
			if(!findPerformanceIds.contains(perfId))
				throw new BaseException(NOT_TO_DELETE_PLANS);
		});

		// 삭제 및 추가
		List<Performance> toAddPerformances = new ArrayList<>();   //Arrays.emptyList()

		performances.forEach(p -> {
			if(perfIds.contains(p.getPerfId())) {
				performanceRepository.delete(p);
			} else {
				toAddPerformances.add(p);
			}
		});

		return memberId;
	}

	@Override
	public Long deletePerf(String day, Long memberId, List<Integer> seqs) {
		List<Performance> findPerformances = performanceRepository.findByPerfDayAndMemberMemberIdAndSeqIn(day, memberId, seqs);

		findPerformances.forEach(pf -> performanceRepository.deleteById(pf.getPerfId()));

		return memberId;
	}


	// ===================================================================
	// ======================== Internal Use =============================
	// ===================================================================
	private boolean checkIsValidSeqAndHour(List<PerformanceSaveEditReq> perfEditReqs) {
		List<PerformanceSaveEditReq> sortedPerfEditReqs = perfEditReqs.stream()
			.sorted(Comparator.comparing(PerformanceSaveEditReq::getSeq))
			.collect(Collectors.toList());

		for(int cnt = 1; cnt <= perfEditReqs.size(); cnt++) {
			if(sortedPerfEditReqs.get(cnt-1).getSeq() != cnt)
				throw new BaseException(BaseResponseStatus.INVALID_PERFORMANCE_SEQUENCE);
		}

		if (sortedPerfEditReqs.size() < 0) {
			return true;
		}

		int startHour = Integer.parseInt(sortedPerfEditReqs.get(0).getStartedHour());
		int endHour = Integer.parseInt(sortedPerfEditReqs.get(0).getEndedHour());

		if (sortedPerfEditReqs.size() == 1) {
			if (startHour > endHour) throw new BaseException(BaseResponseStatus.INVALID_PERFORMANCE_HOUR);
		}

		for(int i = 0; i < sortedPerfEditReqs.size() - 1; i++) {
			int startedHour = Integer.parseInt(sortedPerfEditReqs.get(i).getStartedHour());
			int endedHour = Integer.parseInt(sortedPerfEditReqs.get(i).getEndedHour());

			int nextStartHour = Integer.parseInt(sortedPerfEditReqs.get(i+1).getStartedHour());
			int nextEndHour = Integer.parseInt(sortedPerfEditReqs.get(i+1).getEndedHour());
			int lunchTime = 1200;

			if (endedHour != nextStartHour) {
				if (endedHour != lunchTime || nextStartHour != lunchTime + 100) throw new BaseException(BaseResponseStatus.INVALID_PERFORMANCE_HOUR);
			}

			if (startedHour > endedHour) throw new BaseException(BaseResponseStatus.INVALID_PERFORMANCE_HOUR);

			if (endedHour > nextStartHour) throw new BaseException(BaseResponseStatus.INVALID_PERFORMANCE_HOUR);

			if (nextStartHour > nextEndHour) throw new BaseException(BaseResponseStatus.INVALID_PERFORMANCE_HOUR);
		}

		return true;
	}

}
