package com.hjun.timereport.global.job.approval;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.hjun.timereport.performance.entity.Performance;
import com.hjun.timereport.performance.repository.PerformanceRepository;

import lombok.extern.slf4j.Slf4j;

/*
	Batch Job : 확정한 실적에 대해서 전일자 실적 자동 승인
 */
@Slf4j
@Configuration
public class ApprovalBatchConfig {

	public static final String APPROVAL_JOB_PREFIX = "performance";
	public static final String APPROVAL_JOB_NAME = "ApprovalJob";

	private static final int CHUNK_SIZE = 100;

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private final EntityManagerFactory entityManagerFactory;
	private final PerformanceRepository performanceRepository;

	public ApprovalBatchConfig(JobBuilderFactory jobBuilderFactory,
		StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory,
		PerformanceRepository performanceRepository) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.entityManagerFactory = entityManagerFactory;
		this.performanceRepository = performanceRepository;
	}

	@Bean(APPROVAL_JOB_PREFIX + APPROVAL_JOB_NAME)
	public Job ApprovalYesterdayJob() {

		return jobBuilderFactory.get("ApprovalJob")
			.start(ApprovalYesterdayStep())
			.build();
	}

	@Bean(APPROVAL_JOB_PREFIX + "ApprovalStep")
	@JobScope
	public Step ApprovalYesterdayStep() {

		return stepBuilderFactory.get("ApprovalStep")
			.<Performance, Performance>chunk(CHUNK_SIZE)
			.reader(jpaPagingPerformanceReader())
			.writer(performanceJpaItemWriter())
			.build();
	}

	@Bean(APPROVAL_JOB_PREFIX + "approvalReader")
	@StepScope
	@Retryable(value = Throwable.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, maxDelay = 2000))
	public JpaPagingItemReader<Performance> jpaPagingPerformanceReader() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String yesterday = LocalDate.now().minusDays(1L).format(formatter);

		log.info("[+] =======> Yesterday = {}", yesterday);

		return new JpaPagingItemReaderBuilder<Performance>()
			.name("approvalReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("SELECT p FROM Performance p WHERE perf_day=" + yesterday + " and sign_status = 2")
			.pageSize(CHUNK_SIZE)
			.build();
	}

	// sign_status 2(확정)을 3(자동승인)
	@Bean(APPROVAL_JOB_PREFIX + "approvalWriter")
	@StepScope
	@Retryable(value = Throwable.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, maxDelay = 2000))
	public ItemWriter<Performance> performanceJpaItemWriter() {
		log.info("[+] start Writer");

		return list -> {
			for (Performance p : list) {
				log.info("[+] curr -> {}", p);
				performanceRepository.approvalAllPerformances(p.getPerfId());
			}
		};

	}
}
