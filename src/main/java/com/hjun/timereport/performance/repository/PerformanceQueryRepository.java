package com.hjun.timereport.performance.repository;

import static com.hjun.timereport.biweekly.entity.QHoliday.holiday;
import static com.hjun.timereport.deadline.entity.QDeadline.deadline;
import static com.hjun.timereport.performance.entity.QPerformance.performance;
import static org.springframework.util.StringUtils.hasText;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.dto.PerformanceDto;
import com.hjun.timereport.performance.entity.QPerformance;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PerformanceQueryRepository {

	private final JPAQueryFactory queryFactory;


	public List<PerformanceDto> findMemberAndPerfDay(Member member, String startDate, String endDate) {

		QPerformance p = QPerformance.performance;


		return queryFactory.select(Projections.constructor(PerformanceDto.class,
				QPerformance.performance.perfId,
				p.seq,
				p.taskHour,
				p.perfDay.castToNum(Integer.class),
				p.dayHour,
				p.startedHour,
				p.endedHour,
				p.groupMainId,
				p.groupSubId,
				p.codeId,
				p.codeMainNm,
				p.codeSubNm,
				p.workDetail,
				p.signStatus,
				p.breakTime,
				p.overtimeDetail,
				p.wfhYn,
				holiday.isHoliday,
				p.member.memberId,
				deadline.isDeadline
				))
				.from(p)
				.leftJoin(deadline)
				.on(p.perfDay.eq(deadline.deadlineDay))
				.leftJoin(holiday)
				.on(p.perfDay.eq(holiday.holidayDate))
				.where(
						p.perfDay.between(startDate, endDate)
						.and(p.member.memberId.eq(member.getMemberId()))
				)
				.fetch();
	}

	public Optional<Double> getWeeklyWorkHour(String startDate, String today, Long memberId) {
			return Optional.ofNullable(queryFactory
				.select(performance.dayHour.sum())
				.from(performance)
				.where(eqMemberId(memberId),
						eqPerfDayBetween(startDate, today),
						performance.seq.eq(1))
				.fetchOne());
	}

	private BooleanExpression eqMemberId(Long memberId) {
		return memberId != null ? performance.member.memberId.eq(memberId) : null;
	}

	private BooleanExpression eqPerfDayBetween(String startDate, String today) {
		return hasText(startDate) || hasText(today) ? performance.perfDay.between(startDate, today) : null;
	}

	public boolean findPerformanceSignStatusExists (String day) {
		Integer fetchFirst = queryFactory
				.selectOne()
				.from(performance)
				.where(
						performance.seq.eq(1)
						.and( (performance.signStatus.eq("1")).or(performance.signStatus.eq("2")) )
						.and(performance.perfDay.eq(day))
						)
				.fetchFirst();

		return fetchFirst != null;
	}


}
