package com.hjun.timereport.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hjun.timereport.biweekly.BiWeeklyService;
import com.hjun.timereport.biweekly.dto.TwoWeeksBoxDto;
import com.hjun.timereport.global.job.approval.ApprovalBatchConfig;
import com.hjun.timereport.global.job.twoweeks.TwoWeeksBatchConfig;
import com.hjun.timereport.global.response.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/biweekly")
public class BiWeeklyController {

	private final BiWeeklyService biWeeklyService;
	private final JobLauncher jobLauncher;
	private final TwoWeeksBatchConfig twoWeeksBatchConfig;
	private final ApprovalBatchConfig approvalBatchConfig;

	public BiWeeklyController(BiWeeklyService biWeeklyService,
		JobLauncher jobLauncher, TwoWeeksBatchConfig twoWeeksBatchConfig,
		ApprovalBatchConfig approvalBatchConfig) {
		this.biWeeklyService = biWeeklyService;
		this.jobLauncher = jobLauncher;
		this.twoWeeksBatchConfig = twoWeeksBatchConfig;
		this.approvalBatchConfig = approvalBatchConfig;
	}

	@GetMapping
	public BaseResponse<TwoWeeksBoxDto> findTwoWeeks() {
		return new BaseResponse<>(biWeeklyService.findTwoWeeks());
	}

	@GetMapping("/week-job")
	public void twoWeeksBatchJob() {
		JobExecution execution;
		try {
			log.info(" >> [+] Start report Job");
			execution = jobLauncher.run(twoWeeksBatchConfig.TwoWeeksJob(), simpleJobParam());
			log.info(" >> [+] Job finished with status : " + execution.getStatus());
			log.info(" >> [+] Current Thread: {}", Thread.currentThread().getName());
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@GetMapping("/confirm-job")
	public void approvalPerformanceYesterday() {
		JobExecution execution;
		try {
			log.info(" >> [+] Start report Job");
			execution = jobLauncher.run(approvalBatchConfig.ApprovalYesterdayJob(), simpleJobParam());
			log.info(" >> [+] Job finished with status : " + execution.getStatus());
			log.info(" >> [+] Current Thread: {}", Thread.currentThread().getName());
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	// 같은 이름의 batch는 생길 수 없기 때문에 param에 시간을 넣는다.
	private JobParameters simpleJobParam() {
		Map<String, JobParameter> confMap = new HashMap<>();
		confMap.put("time", new JobParameter(System.currentTimeMillis()));
		return new JobParameters(confMap);
	}

}
