package com.hjun.timereport.plan;

import static com.hjun.timereport.global.response.BaseResponseStatus.NOT_TO_DELETE_PLANS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjun.timereport.biweekly.BiWeeklyService;
import com.hjun.timereport.global.exception.BaseException;
import com.hjun.timereport.global.response.BaseResponseStatus;
import com.hjun.timereport.member.MemberService;
import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.PerformanceService;
import com.hjun.timereport.performance.dto.PerformanceDto;
import com.hjun.timereport.plan.dto.PlanDaysReq;
import com.hjun.timereport.plan.dto.PlanDto;
import com.hjun.timereport.plan.dto.PlanSaveReq;
import com.hjun.timereport.plan.entity.Plan;
import com.hjun.timereport.plan.repository.PlanRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService {

	private final PlanRepository planRepository;
	private final PerformanceService performanceService;
	private final BiWeeklyService biWeeklyService;
	private final MemberService memberService;

	public PlanServiceImpl(PlanRepository planRepository, PerformanceService performanceService,
			BiWeeklyService biWeeklyService, MemberService memberService) {
		this.planRepository = planRepository;
		this.performanceService = performanceService;
		this.biWeeklyService = biWeeklyService;
		this.memberService = memberService;
	}

	@Override
	public List<PlanDto> findPlans(String day, Long memberId) {

		// 1-1. day가 오늘이라면 2주 전체의 주 시작일 가져오기
		// 1-2. 그게 아니라면 day는 주시작일이 보장됨, 따라서 주시작일 가져올 필요 X

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String startDay = LocalDate.now().format(formatter);

		if (!day.isBlank()) {
			startDay = day;
		}
		biWeeklyService.isValidDate(startDay);

		startDay = biWeeklyService.findStartOfWeekday(startDay);


		String endDay = LocalDate.parse(startDay, formatter).plusDays(13).format(formatter);


		// 2. Between 이용하여 day의 시작일과 day+13일(종료일) + memberId를 이용하여 계획 리스트 가져오기
		List<Plan> findPlans = planRepository.findByMemberMemberIdAndPlanDayBetween(memberId, startDay, endDay);


		Map<String, List<Plan>> findPlanMap = findPlans.stream()
				.collect(Collectors.groupingBy(Plan::getPlanDay));

		// 3. 휴일 테이블 통해서 휴일 가져오기
		Map<String, String> holidayMap = biWeeklyService.findHolidayBetweenMap(startDay, endDay);

		// 4. (2), (3) 합쳐서 isHoliday 채워준 후에 PlanDto 리스트 생성
		// 14일 전체 돌면서 key값이 없는지 체크
		List<PlanDto> findPlanDtos = findPlans.stream()
			.map(p -> PlanDto.entityToDto(p, holidayMap.getOrDefault(p.getPlanDay(), "Y")))
			.collect(Collectors.toList());

		Stream.iterate(LocalDate.parse(startDay, formatter), t -> t.plusDays(1))
				.limit(14)
				.filter(t -> !findPlanMap.containsKey(t.format(formatter)))
				.forEach(t -> findPlanDtos.add(PlanDto.dummyDto(t.format(formatter), holidayMap.getOrDefault(t.format(formatter), "Y"))));

		return findPlanDtos.stream()
				.sorted(Comparator.comparing(PlanDto::getPlanDay))
				.collect(Collectors.toList());
	}

	@Override
	public List<PlanDto> findPlanDetails(PlanDaysReq planDaysReq, Long memberId) {

		// [선택한 일정의 세부계획 가져오기]

		// 1. planDay와 Id를 통해서 계획 리스트 받아오기
		// findByMemberIdAndPlanDayIn
		List<Plan> findPlans = planRepository.findByMemberMemberIdAndPlanDayIn(memberId, planDaysReq.getPlanDays());

		// 2. 휴일 테이블 통해서 휴일 가져오기
		Map<String, String> holidayMap = biWeeklyService.findHolidayInMap(planDaysReq.getPlanDays());

		// 3. (1), (2) 합쳐서 isHoliday 채워준 후에 PlanDto 리스트 생성
		return findPlans.stream()
			.map(p -> PlanDto.entityToDto(p, holidayMap.getOrDefault(p.getPlanDay(), "Y")))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Long draftPlan(List<PlanSaveReq> planSaveReqs, Long memberId) {

		saveAndEditPlans(planSaveReqs, memberId, "0");


		return memberId;
	}

	@Override
	@Transactional
	public Long draftPlanOld(List<PlanSaveReq> planSaveReqs, Long memberId) {

		saveAndEditPlansOld(planSaveReqs, memberId, "0");


		return memberId;
	}


	@Override
	@Transactional
	public Long editPlan(List<PlanSaveReq> planSaveReqs, Long memberId) {

		saveAndEditPlans(planSaveReqs, memberId, "1");


		// [수정 (사실상 등록) - 실적에도 반영]
		Map<String, Double> workHourPerDayMap = planSaveReqs.stream()
				.collect(Collectors.groupingBy(PlanSaveReq::getPlanDay, Collectors.summingDouble(PlanSaveReq::getTaskHour)));


		List<PerformanceDto> performanceSaveReqs = planSaveReqs.stream()
			.map(psr -> PerformanceDto.builder()
					.seq(psr.getSeq())
					.taskHour(psr.getTaskHour())
					.perfDay(Integer.parseInt(psr.getPlanDay()))
					.dayHour(workHourPerDayMap.get(psr.getPlanDay()))
					.startedHour(psr.getStartedHour())
					.endedHour(psr.getEndedHour())
					.groupMainId(psr.getGroupMainId())
					.groupSubId(psr.getGroupSubId())
					.codeId(psr.getCodeId())
					.codeMainNm(psr.getCodeMainNm())
					.codeSubNm(psr.getCodeSubNm())
					.workDetail(psr.getWorkDetail())
					.wfhYn(psr.getWfhYn())
					.breakTime(1.0)
					.signStatus("1")
					.overtimeDetail("")
					.memberId(memberId)
					.build()
			).collect(Collectors.toList());


		performanceService.savePerformancesFromPlan(performanceSaveReqs, memberId);


		// 4. memberId 리턴
		return memberId;
	}

	@Override
	public boolean isExistPlanDayBetweenBy(String startDay, String endDay, Long memberId) {
		return planRepository.existsByMemberMemberIdAndPlanDayBetween(memberId, startDay, endDay);
	}

	private void saveAndEditPlans(List<PlanSaveReq> planSaveReqs, Long memberId, String enrollYn) {
		// [임시저장 (계획에만 반영)
			// 1. planSaveReq 에서 날짜를 얻음
			List<String> planDays = planSaveReqs.stream()
				.map(PlanSaveReq::getPlanDay)
				.map(Object::toString)
				.collect(Collectors.toList());


			// 2. planSaveReq에서 금일 일한 총 시간 합계를 구함
			Map<String, Double> workHourPerDayMap = planSaveReqs.stream()
					.collect(Collectors.groupingBy(PlanSaveReq::getPlanDay, Collectors.summingDouble(PlanSaveReq::getTaskHour)));

			// 3. planSaveReq에서 날짜In과 MemberId로 가져와서 영속성 올리기 => findPlans
			List<Plan> findPlans = planRepository.findByMemberMemberIdAndPlanDayIn(memberId, planDays);

			// 4. planID가 없는 elements -> 저장할 것들
			// TODO: 오늘 날짜 이상에만 계획 수정 가능하도록 filter 넣기 (dropwhile)
			planSaveReqs.stream()
				.filter(psr -> psr.getPlanId() == 0L)
				.forEach(psr -> {
					Member member = memberService.findMember(memberId);

					Plan plan = Plan.builder()
						.seq(psr.getSeq())
						.taskHour(psr.getTaskHour())
						.planDay(psr.getPlanDay())
						.dayHour(workHourPerDayMap.get(psr.getPlanDay()))
						.startedHour(psr.getStartedHour())
						.endedHour(psr.getEndedHour())
						.groupMainId(psr.getGroupMainId())
						.groupSubId(psr.getGroupSubId())
						.codeId(psr.getCodeId())
						.codeMainNm(psr.getCodeMainNm())
						.codeSubNm(psr.getCodeSubNm())
						.workDetail(psr.getWorkDetail())
						.wfhYn(psr.getWfhYn())
						.enrollYn(enrollYn)
						.member(member)
						.build();

					planRepository.save(plan);
				});

			// 5. planID가 있는 elements -> 수정할 것들 (hashMap 변경)
			// TODO: 오늘 날짜 이상에만 계획 수정 가능하도록 filter 넣기 (dropwhile)
			Map<Long, PlanSaveReq> planIdMap = planSaveReqs.stream()
					.filter(psr -> psr.getPlanId() != 0L)
					.collect(Collectors.toMap(PlanSaveReq::getPlanId, o -> o));

			findPlans.forEach(plan -> {
				try {
					if (planIdMap.containsKey(plan.getPlanId())) {
						plan.editPlan(planIdMap.get(plan.getPlanId()), enrollYn);
					}

				} catch (Exception e) {
					throw new BaseException(BaseResponseStatus.WRONG_ACCESS);
				}
			});
	}

	private void saveAndEditPlansOld(List<PlanSaveReq> planSaveReqs, Long memberId, String enrollYn) {
		// [임시저장 (계획에만 반영)
		// 1. planSaveReq 에서 날짜를 얻음
		List<String> planDays = planSaveReqs.stream()
			.map(PlanSaveReq::getPlanDay)
			.map(Object::toString)
			.collect(Collectors.toList());


		// 2. planSaveReq에서 금일 일한 총 시간 합계를 구함
		Map<String, Double> workHourPerDayMap = planSaveReqs.stream()
			.collect(Collectors.groupingBy(PlanSaveReq::getPlanDay, Collectors.summingDouble(PlanSaveReq::getTaskHour)));

		// 3. planSaveReq에서 날짜In과 MemberId로 가져와서 영속성 올리기 => findPlans
		List<Plan> findPlanList = planRepository.findByMemberMemberIdAndPlanDayIn(memberId, planDays);

		// 4. planID가 없는 elements -> 저장할 것들
		// TODO: 오늘 날짜 이상에만 계획 수정 가능하도록 filter 넣기 (dropwhile)
		planSaveReqs.stream()
			.filter(psr -> psr.getPlanId() == 0L)
			.forEach(psr -> {
				Member member = memberService.findMember(memberId);

				Plan plan = Plan.builder()
					.seq(psr.getSeq())
					.taskHour(psr.getTaskHour())
					.planDay(psr.getPlanDay())
					.dayHour(workHourPerDayMap.get(psr.getPlanDay()))
					.startedHour(psr.getStartedHour())
					.endedHour(psr.getEndedHour())
					.groupMainId(psr.getGroupMainId())
					.groupSubId(psr.getGroupSubId())
					.codeId(psr.getCodeId())
					.codeMainNm(psr.getCodeMainNm())
					.codeSubNm(psr.getCodeSubNm())
					.workDetail(psr.getWorkDetail())
					.wfhYn(psr.getWfhYn())
					.enrollYn(enrollYn)
					.member(member)
					.build();

				planRepository.save(plan);
			});

		planSaveReqs.stream()
			.filter(psr -> psr.getPlanId() != 0L)
			.forEach(psr -> {
				findPlanList.stream()
					.forEach(p -> {
						if (psr.getPlanId().equals(p.getPlanId())) {
						 	p.editPlan(psr, enrollYn);
						}
					});
			});
	}

	@Override
	@Transactional
	public Long deletePlans(List<Long> planIds, Long memberId, String day) {

		/*
		 1. 해당 날짜에 해당하는 plan List를 가져온다.
		*/
		List<Plan> plans = planRepository.findByPlanDayAndMemberMemberId(day, memberId);
		List<Long> findPlanIds = plans.stream().map(Plan::getPlanId).collect(Collectors.toList());

		/*

			2. planIds가 plan List에 있는지 확인한다. 없으면 throw 던짐 (Deque)
			 Queue<Plan> toDeletePlanQueue = new Queue<>();  혹은 List로 만들어서 처리

			 contain체크 없으면 throw 있으면 planQueue에 push pop)
		  	(plans에 남아있는 것들은 performance에 저장할 것들)

		 */

		// 검증
		planIds.forEach(planId -> {
			if(!findPlanIds.contains(planId))
				throw new BaseException(NOT_TO_DELETE_PLANS);
		});


		// 삭제 및 추가
		List<Plan> toAddPlans = new ArrayList<>();   //Arrays.emptyList()
		plans.forEach(p -> {
			if(planIds.contains(p.getPlanId())) {
				planRepository.delete(p);
			} else {
				toAddPlans.add(p);
			}
		});


		List<PerformanceDto> performanceSaveReqs = toAddPlans.stream()
			.map(psr -> PerformanceDto.builder()
					.seq(psr.getSeq())
					.taskHour(psr.getTaskHour())
					.perfDay(Integer.parseInt(psr.getPlanDay()))
					.dayHour(psr.getDayHour())
					.startedHour(psr.getStartedHour())
					.endedHour(psr.getEndedHour())
					.groupMainId(psr.getGroupMainId())
					.groupSubId(psr.getGroupSubId())
					.codeId(psr.getCodeId())
					.codeMainNm(psr.getCodeMainNm())
					.codeSubNm(psr.getCodeSubNm())
					.workDetail(psr.getWorkDetail())
					.wfhYn(psr.getWfhYn())
					.breakTime(1.0)
					.signStatus("1")
					.overtimeDetail("")
					.memberId(memberId)
					.build()
		).collect(Collectors.toList());


		performanceService.savePerformancesFromPlan(performanceSaveReqs, memberId);


		return memberId;
	}

}
