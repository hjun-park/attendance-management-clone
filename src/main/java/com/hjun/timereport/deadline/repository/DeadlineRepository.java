package com.hjun.timereport.deadline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hjun.timereport.deadline.entity.Deadline;

public interface DeadlineRepository extends JpaRepository<Deadline, String> {

	List<Deadline> findByDeadlineDayBetween(String firstDay, String lastDay);

	Deadline findByDeadlineDay(String eachDay);

}
