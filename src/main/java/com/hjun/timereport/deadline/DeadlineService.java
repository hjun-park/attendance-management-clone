package com.hjun.timereport.deadline;

import java.util.List;

import com.hjun.timereport.deadline.dto.DeadlineDto;
import com.hjun.timereport.deadline.dto.DeadlineRegisterDto;
import com.hjun.timereport.deadline.dto.DeadlineReq;
import com.hjun.timereport.deadline.entity.Deadline;

public interface DeadlineService {

	List<DeadlineDto> findDeadline(String year, String month);

	DeadlineRegisterDto registerDeadline(DeadlineReq deadlineReq);

	Deadline findByDeadlineDay(String eachDay);

	List<Deadline> findByDeadlineDayBetween(String firstDay, String lastDay);

}
