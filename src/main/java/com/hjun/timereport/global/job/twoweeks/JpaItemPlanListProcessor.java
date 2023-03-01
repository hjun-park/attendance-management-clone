package com.hjun.timereport.global.job.twoweeks;

import com.hjun.timereport.biweekly.BiWeeklyService;
import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.plan.PlanService;
import com.hjun.timereport.plan.entity.Plan;
import com.hjun.timereport.task.TaskService;
import com.hjun.timereport.task.dto.TaskDto;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JpaItemPlanListProcessor implements ItemProcessor<Member, List<Plan>> {

    private final BiWeeklyService biWeeklyService;
    private final PlanService planService;
    private final TaskService taskService;


    @Builder
    public JpaItemPlanListProcessor(BiWeeklyService biWeeklyService, PlanService planService, TaskService taskService) {
		this.biWeeklyService = biWeeklyService;
		this.planService = planService;
		this.taskService = taskService;
	}


	@Override
	@Retryable(value = Throwable.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, maxDelay = 2000))
    public List<Plan> process(Member item) throws Exception {

        // 날짜 순회
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = LocalDate.now().format(formatter);

        String startDay = biWeeklyService.findStartOfWeekday(today);
        String endDay = LocalDate.parse(startDay, formatter).plusDays(13).format(formatter);

        boolean existPlanDayBetweenBy = planService.isExistPlanDayBetweenBy(startDay, endDay, item.getMemberId());

        if (existPlanDayBetweenBy) {
            return new ArrayList<>();
        }

        // 기본 Task 불러오기
        TaskDto taskDto = taskService.getDefaultTaskDto();

        List<String> findWeekdays = biWeeklyService.findHolidayBetweenMap(startDay, endDay).entrySet().stream()
                .filter(e -> e.getValue().equals("N"))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return findWeekdays.stream()
                .map(weekday -> {
                            return Plan.builder()
                                    .seq(1)
                                    .taskHour(8.0)
                                    .planDay(weekday)
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
                                    .enrollYn("1")
                                    .member(item)
                                    .build();
                        }
                ).collect(Collectors.toList());
    }

}
