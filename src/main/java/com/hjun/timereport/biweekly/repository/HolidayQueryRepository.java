package com.hjun.timereport.biweekly.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hjun.timereport.biweekly.entity.QHoliday;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HolidayQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Map<String, String> findHolidayBetween(String startDay, String endDay) {

		return queryFactory.selectFrom(QHoliday.holiday)
				.where(QHoliday.holiday.holidayDate.between(startDay, endDay))
				.orderBy(QHoliday.holiday.holidayDate.asc())
				.transform(GroupBy.groupBy(QHoliday.holiday.holidayDate).as(QHoliday.holiday.isHoliday));
	}

	public Map<String, String> findHolidayIn(List<String> days) {
		return queryFactory.selectFrom(QHoliday.holiday)
				.where(QHoliday.holiday.holidayDate.in(days))
				.transform(GroupBy.groupBy(QHoliday.holiday.holidayDate).as(QHoliday.holiday.isHoliday));
	}


}
