package com.hjun.timereport.global.job.twoweeks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.hjun.timereport.biweekly.BiWeeklyService;
import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.PerformanceService;
import com.hjun.timereport.performance.entity.Performance;
import com.hjun.timereport.task.TaskService;
import com.hjun.timereport.task.dto.TaskDto;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaItemPerfListProcessor implements ItemProcessor<Member, List<Performance>> {

	private final BiWeeklyService biWeeklyService;
	private final PerformanceService performanceService;
	private final TaskService taskService;

	@Builder
	public JpaItemPerfListProcessor(BiWeeklyService biWeeklyService, PerformanceService performanceService,
		TaskService taskService) {
		this.biWeeklyService = biWeeklyService;
		this.performanceService = performanceService;
		this.taskService = taskService;
	}

	@Override
	@Retryable(value = Throwable.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, maxDelay = 2000))
	public List<Performance> process(Member item) throws Exception {

		// 날짜 순회
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String today = LocalDate.now().format(formatter);

		String startDay = biWeeklyService.findStartOfWeekday(today);
		String endDay = LocalDate.parse(startDay, formatter).plusDays(13).format(formatter);

		if (performanceService.isExistPerfDayBetweenBy(startDay, endDay, item.getMemberId())) {
			log.info("!!!!!!!!!!!!!!!!!!!!!![Performance] memberId 추가 X => {}", item.getMemberId());
			return new ArrayList<>();
		}

		// 기본 Task 불러오기
		TaskDto taskDto = taskService.getDefaultTaskDto();

		List<String> findWeekdays = biWeeklyService.findHolidayBetweenMap(startDay, endDay).entrySet().stream()
			.filter(e -> e.getValue().equals("N"))
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());

		List<Performance> performances = findWeekdays.stream()
			.map(weekday -> {
					return Performance.builder()
						.seq(1)
						.taskHour(8.0)
						.perfDay(weekday)
						.dayHour(8.0)
						.startedHour("0900")
						.endedHour("1800")
						.groupMainId(taskDto.getGroupMainId())
						.groupSubId(taskDto.getGroupSubId())
						.codeId(taskDto.getCodeId())
						.codeMainNm(taskDto.getCodeMainNm())
						.codeSubNm(taskDto.getCodeSubNm())
						.workDetail("주업무")
						.wfhYn("0")
						.signStatus("1")
						.breakTime(1.0)
						.member(item)
						.build();
				}
			).collect(Collectors.toList());

		return performances;
	}

}
