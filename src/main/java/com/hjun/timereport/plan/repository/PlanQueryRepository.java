package com.hjun.timereport.plan.repository;

import static com.hjun.timereport.plan.entity.QPlan.plan;
import static org.springframework.util.StringUtils.hasText;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlanQueryRepository {

	private final JPAQueryFactory queryFactory;


	public Optional<Double> getWeeklyWorkHour(String today, String endDay, Long memberId) {
		return Optional.ofNullable(queryFactory
			.select(plan.dayHour.sum())
			.from(plan)
			.where(eqMemberId(memberId),
					eqPlanDayBetween(today, endDay),
					plan.seq.eq(1))
			.fetchOne());
	}

	private BooleanExpression eqMemberId(Long memberId) {
		return memberId != null ? plan.member.memberId.eq(memberId) : null;
	}

	private BooleanExpression eqPlanDayBetween(String today, String endDay) {
		return hasText(today) || hasText(endDay) ? plan.planDay.between(today, endDay) : null;
	}

}
