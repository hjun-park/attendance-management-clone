package com.hjun.timereport.global.job.twoweeks;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableBatchProcessing
public class TwoWeeksJobTestConfiguration {
	// @Profile("local")
	// @Bean
	// @Qualifier("chunkSize")
	// public Integer batchContext() {
	// 	return 10;
	// }
	@Bean
	@Qualifier("twoWeeksWriteJob")
	JobLauncherTestUtils twoWeeksJobUtils() {
		return new JobLauncherTestUtils() {
			@Override
			@Autowired
			public void setJob(@Qualifier("twoWeeksWriteJob") Job job) {
				super.setJob(job);
			}
		};
	}
}
