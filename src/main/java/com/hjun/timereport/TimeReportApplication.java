package com.hjun.timereport;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.hjun.timereport.global.util.SlackProperties;

@EnableRetry
@EnableJpaAuditing
@EnableJpaRepositories
@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
@EnableRedisHttpSession
@EnableConfigurationProperties(value = {SlackProperties.class})
@EntityScan(basePackages = {"com.hjun.timereport"})
public class TimeReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeReportApplication.class, args);
	}

}
