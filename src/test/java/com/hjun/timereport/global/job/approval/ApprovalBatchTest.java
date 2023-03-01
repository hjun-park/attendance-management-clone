package com.hjun.timereport.global.job.approval;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hjun.timereport.member.repository.MemberRepository;
import com.hjun.timereport.performance.repository.PerformanceRepository;
import com.hjun.timereport.plan.repository.PlanRepository;

@SpringBootTest(classes = {ApprovalJobTestConfiguration.class})
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
class ApprovalBatchTest {

	@Autowired
	@Qualifier("performanceApprovalJob")
	private Job job;

	@Autowired
	@Qualifier(value = "performanceApprovalJob")
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private PerformanceRepository performanceRepository;

	@Autowired
	private MemberRepository memberRepository;

	@PersistenceContext
	private EntityManager em;


	// @BeforeAll
	// public void setup() {
	//
	// 	performanceRepository.deleteAllInBatch();
	// 	planRepository.deleteAllInBatch();
	//
	// 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	// 	String today = LocalDate.now().format(formatter);
	// 	String yesterday = LocalDate.now().minusDays(1L).format(formatter);
	// 	String dayBeforeYesterday = LocalDate.now().minusDays(2L).format(formatter);
	//
	// 	System.out.println("[+] =====> today = " + today);
	// 	System.out.println("[+] =====> yesterday = " + yesterday);
	// 	System.out.println("[+] =====> dayBeforeYesterday = " + dayBeforeYesterday);
	//
	// 	List<Member> members = memberRepository.findAll();
	//
	// 	// 1-1. 멤버 짝수 ID에 대해서 전날 확정 데이터 집어넣기
	// 	List<Performance> confirmedYesterdayPerf = members.stream()
	// 		.filter(m -> m.getMemberId() % 2 == 0)
	// 		.map(m -> Performance.builder()
	// 			.seq(1)
	// 			.taskHour(8.0)
	// 			.perfDay(yesterday)
	// 			.dayHour(8.0)
	// 			.startedHour("0900")
	// 			.endedHour("1800")
	// 			.groupMainId("TR001")
	// 			.groupSubId("ZDUM1")
	// 			.codeId("Z001")
	// 			.codeMainNm("주업무")
	// 			.codeSubNm("R&D 및 내부 PJT (NonPJT코드) - 시장조사, 분석, 계획, 설계/개발/테스트/이행")
	// 			.workDetail("테스트 데이터")
	// 			.signStatus("2")
	// 			.breakTime(1.0)
	// 			.overtimeDetail(null)
	// 			.wfhYn("0")
	// 			.member(m)
	// 			.build()
	// 		).collect(Collectors.toList());
	//
	// 	performanceRepository.saveAll(confirmedYesterdayPerf);
	//
	// 	// 1-2. 남은 멤버 홀수 ID에 대해서 전날 미확정(저장) 데이터 집어넣기
	// 	List<Performance> savedYesterdayPerf = members.stream()
	// 		.filter(m -> m.getMemberId() % 2 != 0)
	// 		.map(m -> Performance.builder()
	// 			.seq(1)
	// 			.taskHour(8.0)
	// 			.perfDay(yesterday)
	// 			.dayHour(8.0)
	// 			.startedHour("0900")
	// 			.endedHour("1800")
	// 			.groupMainId("TR001")
	// 			.groupSubId("ZDUM1")
	// 			.codeId("Z001")
	// 			.codeMainNm("주업무")
	// 			.codeSubNm("R&D 및 내부 PJT (NonPJT코드) - 시장조사, 분석, 계획, 설계/개발/테스트/이행")
	// 			.workDetail("테스트 데이터")
	// 			.signStatus("1")
	// 			.breakTime(1.0)
	// 			.overtimeDetail(null)
	// 			.wfhYn("0")
	// 			.member(m)
	// 			.build()
	// 		).collect(Collectors.toList());
	//
	// 	performanceRepository.saveAll(savedYesterdayPerf);
	//
	// 	// 2-1. 2일 전 멤버 짝수 ID에 대해 확정 데이터 넣기
	// 	List<Performance> confirmedDayBeforeYesterdayPerf = members.stream()
	// 		.filter(m -> m.getMemberId() % 2 == 0)
	// 		.map(m -> Performance.builder()
	// 			.seq(1)
	// 			.taskHour(8.0)
	// 			.perfDay(dayBeforeYesterday)
	// 			.dayHour(8.0)
	// 			.startedHour("0900")
	// 			.endedHour("1800")
	// 			.groupMainId("TR001")
	// 			.groupSubId("ZDUM1")
	// 			.codeId("Z001")
	// 			.codeMainNm("주업무")
	// 			.codeSubNm("R&D 및 내부 PJT (NonPJT코드) - 시장조사, 분석, 계획, 설계/개발/테스트/이행")
	// 			.workDetail("테스트 데이터")
	// 			.signStatus("2")
	// 			.breakTime(1.0)
	// 			.overtimeDetail(null)
	// 			.wfhYn("0")
	// 			.member(m)
	// 			.build()
	// 		).collect(Collectors.toList());
	//
	// 	performanceRepository.saveAll(confirmedDayBeforeYesterdayPerf);
	//
	// 	// 2-2. 2일 전 남은 멤버 홀수 ID에 대해 미확정(저장) 데이터 넣기
	// 	List<Performance> savedDayBeforeYesterdayPerf = members.stream()
	// 		.filter(m -> m.getMemberId() % 2 != 0)
	// 		.map(m -> Performance.builder()
	// 			.seq(1)
	// 			.taskHour(8.0)
	// 			.perfDay(dayBeforeYesterday)
	// 			.dayHour(8.0)
	// 			.startedHour("0900")
	// 			.endedHour("1800")
	// 			.groupMainId("TR001")
	// 			.groupSubId("ZDUM1")
	// 			.codeId("Z001")
	// 			.codeMainNm("주업무")
	// 			.codeSubNm("R&D 및 내부 PJT (NonPJT코드) - 시장조사, 분석, 계획, 설계/개발/테스트/이행")
	// 			.workDetail("테스트 데이터")
	// 			.signStatus("1")
	// 			.breakTime(1.0)
	// 			.overtimeDetail(null)
	// 			.wfhYn("0")
	// 			.member(m)
	// 			.build()
	// 		).collect(Collectors.toList());
	//
	// 	performanceRepository.saveAll(savedDayBeforeYesterdayPerf);
	//
	// }
	//
	// @Test
	// @DisplayName("001. 데이터 입력 확인")
	// @Order(1)
	// public void countData() throws Exception {
	// 	//given
	// 	long memberCount = memberRepository.count();
	// 	long performanceCount = performanceRepository.count();
	//
	// 	//when
	// 	long total = memberCount * 2;
	//
	// 	//then
	// 	Assertions.assertEquals(total, performanceCount);
	// }
	//
	// @Test
	// @Order(2)
	// @DisplayName("002. 자동승인-배치 테스트")
	// public void Approval_배치테스트() throws Exception {
	// 	// log.info("=============== > START TEST < ================");
	//
	// 	//given
	// 	Map<String, JobParameter> confMap = new HashMap<>();
	// 	confMap.put("time", new JobParameter(System.currentTimeMillis()));
	// 	JobParameters jobParameters = new JobParameters(confMap);
	//
	// 	//when
	// 	JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
	//
	// 	//then
	// 	Assertions.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
	// }
	//
	// @Test
	// @DisplayName("003. checkData")
	// @Order(3)
	// public void Approval_데이터확인() throws Exception {
	//
	// 	//given
	// 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	// 	String yesterday = LocalDate.now().minusDays(1L).format(formatter);
	// 	String dayBeforeYesterday = LocalDate.now().minusDays(2L).format(formatter);
	//
	// 	//when
	// 	long countConfirmEvenMember = memberRepository.findAll().stream().filter(m -> m.getMemberId() % 2 == 0).count();
	// 	long countSaveOddMember = memberRepository.findAll().stream().filter(m -> m.getMemberId() % 2 != 0).count();
	//
	// 	int countSaveYesterday = performanceRepository.countBySignStatusAndPerfDay("1", yesterday);
	// 	int countConfirmYesterday = performanceRepository.countBySignStatusAndPerfDay("2", yesterday);
	// 	int countApprovalYesterday = performanceRepository.countBySignStatusAndPerfDay("3", yesterday);
	//
	// 	int countSaveDayBeforeYesterday = performanceRepository.countBySignStatusAndPerfDay("1", dayBeforeYesterday);
	// 	int countConfirmDayBeforeYesterday = performanceRepository.countBySignStatusAndPerfDay("2", dayBeforeYesterday);
	// 	int countApprovalDayBeforeYesterday = performanceRepository.countBySignStatusAndPerfDay("3", dayBeforeYesterday);
	//
	// 	//then
	// 	Assertions.assertEquals(countApprovalYesterday, countConfirmEvenMember); // 전날 짝수멤버는 승인 상태
	// 	Assertions.assertEquals(countSaveYesterday, countSaveOddMember); // 전날 홀수멤버는 저장 상태
	// 	Assertions.assertEquals(countConfirmYesterday, 0); // 전날 승인이 안 된 짝수멤버는 없음
	//
	// 	Assertions.assertEquals(countConfirmDayBeforeYesterday, countConfirmEvenMember); // 전전날 짝수 확정 상태
	// 	Assertions.assertEquals(countSaveDayBeforeYesterday, countSaveOddMember); // 전전날 홀수멤버는 저장 상태
	// 	Assertions.assertEquals(countApprovalDayBeforeYesterday, 0); // 전전날 승인이 된 짝수멤버는 없음
	//
	// }
	//
	// // Rollback 되지 않기 때문에 after 이용하여 지워준다.
	// @AfterAll
	// public void afterDelete() {
	// 	performanceRepository.deleteAllInBatch();
	// 	planRepository.deleteAllInBatch();
	// }
}
