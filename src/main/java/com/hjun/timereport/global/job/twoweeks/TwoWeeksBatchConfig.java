package com.hjun.timereport.global.job.twoweeks;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.hjun.timereport.biweekly.BiWeeklyService;
import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.PerformanceService;
import com.hjun.timereport.performance.entity.Performance;
import com.hjun.timereport.plan.PlanService;
import com.hjun.timereport.plan.entity.Plan;
import com.hjun.timereport.task.TaskService;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Configuration
public class TwoWeeksBatchConfig {

	public static final String TWO_WEEKS_JOB_PREFIX = "twoWeeks";
	public static final String TWO_WEEKS_JOB_NAME = "WriteJob";

	private static final int CHUNK_SIZE = 10;

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private final BiWeeklyService biWeeklyService;
	private final TaskService taskService;
	private final PlanService planService;
	private final PerformanceService performanceService;
	private final EntityManagerFactory entityManagerFactory;

	public TwoWeeksBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
		BiWeeklyService biWeeklyService, TaskService taskService, PlanService planService,
		PerformanceService performanceService, EntityManagerFactory entityManagerFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.biWeeklyService = biWeeklyService;
		this.taskService = taskService;
		this.planService = planService;
		this.performanceService = performanceService;
		this.entityManagerFactory = entityManagerFactory;
	}

	@Bean(TWO_WEEKS_JOB_PREFIX + TWO_WEEKS_JOB_NAME)
	public Job TwoWeeksJob() {

		return jobBuilderFactory.get("WriteJob")
			.start(AddTwoWeeksInfoStep())
			.next(AddPlanOnlyNotEnteredUser())
			.next(AddPerfOnlyNotEnteredUser())
			.build();
	}

	@Bean(TWO_WEEKS_JOB_PREFIX + "addTwoWeeksStep")
	@JobScope
	public Step AddTwoWeeksInfoStep() {

		return stepBuilderFactory.get("addTwoWeeksStep")
			.tasklet((contribution, chunkContext) -> {
				biWeeklyService.saveTwoWeeks();
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean(TWO_WEEKS_JOB_PREFIX + "addPlanStep")
	@JobScope
	public Step AddPlanOnlyNotEnteredUser() {

		return stepBuilderFactory.get("addPlanStep")
			.<Member, List<Plan>>chunk(CHUNK_SIZE)
			.reader(jpaPagingMemberReader())
			.processor(memberPlanProcessor())
			.writer(planWriterList())
			// .faultTolerant()
			// .retry(Throwable.class)
			// .retryLimit(2)
			.build();
	}

	@Bean(TWO_WEEKS_JOB_PREFIX + "addPerfStep")
	@JobScope
	public Step AddPerfOnlyNotEnteredUser() {

		return stepBuilderFactory.get("addPerfStep")
			.<Member, List<Performance>>chunk(CHUNK_SIZE)
			.reader(jpaPagingMemberReader())
			.processor(memberPerfProcessor())
			.writer(perfWriterList())
			// .faultTolerant()
			// .retry(Throwable.class)
			// .retryLimit(2)
			.build();
	}

	@Bean(TWO_WEEKS_JOB_PREFIX + "memberReader")
	@StepScope
	@Retryable(value = Throwable.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, maxDelay = 2000))
	public JpaPagingItemReader<Member> jpaPagingMemberReader() {

		return new JpaPagingItemReaderBuilder<Member>()
			.name("memberReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("select m from Member m")
			.pageSize(CHUNK_SIZE)
			.build();

	}

	@Bean(TWO_WEEKS_JOB_PREFIX + "planProcessor")
	@StepScope
	public ItemProcessor<Member, List<Plan>> memberPlanProcessor() {
		return JpaItemPlanListProcessor.builder()
			.biWeeklyService(biWeeklyService)
			.planService(planService)
			.taskService(taskService)
			.build();

	}

	@Bean(TWO_WEEKS_JOB_PREFIX + "perfProcessor")
	@StepScope
	public ItemProcessor<Member, List<Performance>> memberPerfProcessor() {
		return JpaItemPerfListProcessor.builder()
			.biWeeklyService(biWeeklyService)
			.performanceService(performanceService)
			.taskService(taskService)
			.build();
	}

	@Bean(TWO_WEEKS_JOB_PREFIX + "planWriter")
	@StepScope
	public JpaItemListWriter<Plan> planWriterList() {

		JpaItemWriter<Plan> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);

		return new JpaItemListWriter<>(writer);
	}

	@Bean(TWO_WEEKS_JOB_PREFIX + "perfWriter")
	@StepScope
	public JpaItemListWriter<Performance> perfWriterList() {

		JpaItemWriter<Performance> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);

		return new JpaItemListWriter<>(writer);
	}

}
