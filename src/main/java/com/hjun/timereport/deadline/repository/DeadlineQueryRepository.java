package com.hjun.timereport.deadline.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hjun.timereport.deadline.entity.Deadline;
import com.hjun.timereport.deadline.entity.QDeadline;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeadlineQueryRepository{

	private final JPAQueryFactory queryFactory;

	public List<Deadline> findDeadlineBetween(String firstDay, String lastDay){

		return queryFactory.selectFrom(QDeadline.deadline)
				.where(QDeadline.deadline.deadlineDay.between(firstDay, lastDay))
				.orderBy(QDeadline.deadline.deadlineDay.asc())
				.fetch();
	}






}
