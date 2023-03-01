package com.hjun.timereport.global.job.twoweeks;

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


@SpringBootTest(classes = {TwoWeeksJobTestConfiguration.class})
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
class TwoWeeksBatchTest {

	@Autowired
	@Qualifier("twoWeeksWriteJob")
	private Job job;

	@Autowired
	@Qualifier(value = "twoWeeksWriteJob")
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
	// 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	// 	String today = LocalDate.now().format(formatter);
	//
	// 	System.out.println("[+] =====> today = " + today);
	//
	// 	// 12번 유저에는 임시저장된 계획만 추가 (계획에만 존재)
	// 	Member member12 = memberRepository.findById(12L)
	// 		.orElseThrow(() -> new BaseException(BaseResponseStatus.EMPTY_USER));
	//
	// 	Plan planEnrollN = Plan.builder()
	// 		.seq(1)
	// 		.taskHour(8.0)
	// 		.planDay(today)
	// 		.dayHour(8.0)
	// 		.startedHour("0900")
	// 		.endedHour("1800")
	// 		.groupMainId("TR001")
	// 		.groupSubId("ZDUM1")
	// 		.codeId("Z001")
	// 		.codeMainNm("주업무")
	// 		.codeSubNm("R&D 및 내부 PJT (NonPJT코드) - 시장조사, 분석, 계획, 설계/개발/테스트/이행")
	// 		.workDetail("테스트 데이터")
	// 		.wfhYn("0")
	// 		.enrollYn("0")
	// 		.member(member12)
	// 		.build();
	//
	// 	planRepository.save(planEnrollN);
	//
	// 	// 13번 유저에는 등록된 계획 추가 (실적에도 반영), 실적은 미확정 상태
	// 	Member member13 = memberRepository.findById(13L)
	// 		.orElseThrow(() -> new BaseException(BaseResponseStatus.EMPTY_USER));
	//
	// 	Plan planEnrollY1 = Plan.builder()
	// 		.seq(1)
	// 		.taskHour(8.0)
	// 		.planDay(today)
	// 		.dayHour(8.0)
	// 		.startedHour("0900")
	// 		.endedHour("1800")
	// 		.groupMainId("TR001")
	// 		.groupSubId("ZDUM1")
	// 		.codeId("Z001")
	// 		.codeMainNm("주업무")
	// 		.codeSubNm("R&D 및 내부 PJT (NonPJT코드) - 시장조사, 분석, 계획, 설계/개발/테스트/이행")
	// 		.workDetail("테스트 데이터")
	// 		.wfhYn("0")
	// 		.enrollYn("1")
	// 		.member(member13)
	// 		.build();
	//
	// 	Performance savedPerformance = Performance.builder()
	// 		.seq(1)
	// 		.taskHour(8.0)
	// 		.perfDay(today)
	// 		.dayHour(8.0)
	// 		.startedHour("0900")
	// 		.endedHour("1800")
	// 		.groupMainId("TR001")
	// 		.groupSubId("ZDUM1")
	// 		.codeId("Z001")
	// 		.codeMainNm("주업무")
	// 		.codeSubNm("R&D 및 내부 PJT (NonPJT코드) - 시장조사, 분석, 계획, 설계/개발/테스트/이행")
	// 		.workDetail("테스트 데이터")
	// 		.signStatus("1")
	// 		.breakTime(1.0)
	// 		.overtimeDetail(null)
	// 		.wfhYn("0")
	// 		.member(member13)
	// 		.build();
	//
	// 	planRepository.save(planEnrollY1);
	// 	performanceRepository.save(savedPerformance);
	//
	// 	// 15번 유저에는 등록된 계획 추가 (실적에도 반영), 실적은 확정 상태
	// 	Member member15 = memberRepository.findById(15L)
	// 		.orElseThrow(() -> new BaseException(BaseResponseStatus.EMPTY_USER));
	//
	// 	Plan planEnrollY2 = Plan.builder()
	// 		.seq(1)
	// 		.taskHour(8.0)
	// 		.planDay(today)
	// 		.dayHour(8.0)
	// 		.startedHour("0900")
	// 		.endedHour("1800")
	// 		.groupMainId("TR001")
	// 		.groupSubId("ZDUM1")
	// 		.codeId("Z001")
	// 		.codeMainNm("주업무")
	// 		.codeSubNm("R&D 및 내부 PJT (NonPJT코드) - 시장조사, 분석, 계획, 설계/개발/테스트/이행")
	// 		.workDetail("테스트 데이터")
	// 		.wfhYn("0")
	// 		.enrollYn("1")
	// 		.member(member15)
	// 		.build();
	//
	// 	Performance confirmedPerformance = Performance.builder()
	// 		.seq(1)
	// 		.taskHour(8.0)
	// 		.perfDay(today)
	// 		.dayHour(8.0)
	// 		.startedHour("0900")
	// 		.endedHour("1800")
	// 		.groupMainId("TR001")
	// 		.groupSubId("ZDUM1")
	// 		.codeId("Z001")
	// 		.codeMainNm("주업무")
	// 		.codeSubNm("R&D 및 내부 PJT (NonPJT코드) - 시장조사, 분석, 계획, 설계/개발/테스트/이행")
	// 		.workDetail("테스트 데이터")
	// 		.signStatus("2")
	// 		.breakTime(1.0)
	// 		.overtimeDetail(null)
	// 		.wfhYn("0")
	// 		.member(member15)
	// 		.build();
	//
	// 	planRepository.save(planEnrollY2);
	// 	performanceRepository.save(confirmedPerformance);
	//
	// }
	//
	// @Test
	// @DisplayName("001. 데이터 입력 확인")
	// @Order(1)
	// public void countData() throws Exception {
	// 	//given
	//
	// 	//when
	// 	long countInPlan = planRepository.count();
	// 	long countInPerf = performanceRepository.count();
	//
	// 	//then
	// 	Assertions.assertEquals(3, countInPlan);
	// 	Assertions.assertEquals(2, countInPerf);
	// }
	//
	// @Test
	// @Order(2)
	// @DisplayName("002. 배치 테스트")
	// public void TwoWeeks_배치테스트() throws Exception {
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
	// public void TwoWeeks_데이터확인() throws Exception {
	// 	//given
	// 	// 1. 임시저장한 경우는 무시하고 performance에 반영?
	//
	// 	//when
	// 	// 계획에만 임시저장된 상태
	// 	long countPlan12 = planRepository.countByMemberMemberId(12L);
	// 	long countPerf12 = performanceRepository.countByMemberMemberId(12L);
	//
	// 	// 계획 저장 but 실적 확정 X
	// 	long countPlan13 = planRepository.countByMemberMemberId(13L);
	// 	long countPerf13 = performanceRepository.countByMemberMemberId(13L);
	//
	// 	// 계획 저장 and 실적 확정 O
	// 	long countPlan15 = planRepository.countByMemberMemberId(15L);
	// 	long countPerf15 = performanceRepository.countByMemberMemberId(15L);
	//
	// 	//then
	// 	// 12번은 Perf만 10개
	// 	Assertions.assertEquals(1, countPlan12);
	// 	Assertions.assertEquals(10, countPerf12);
	//
	// 	// 나머지는 데이터가 있으므로 1개씩만
	// 	Assertions.assertEquals(1, countPlan13);
	// 	Assertions.assertEquals(1, countPerf13);
	//
	// 	Assertions.assertEquals(1, countPlan15);
	// 	Assertions.assertEquals(1, countPerf15);
	//
	//
	// }
	//
	// //	@Test
	// //	@Order(1)
	// //	@DisplayName("1. Reader에서의 batch retry 테스트")
	// //	public void Reader_배치시도() throws Exception {
	// //		log.info("=============== > START TEST < ================");
	// //		//given
	// //		Map<String, JobParameter> confMap = new HashMap<>();
	// //		confMap.put("time", new JobParameter(System.currentTimeMillis()));
	// //		JobParameters jobParameters = new JobParameters(confMap);
	// //
	// //		//when
	// //		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
	// //
	// //
	// //		//then
	// //		Assertions.assertNotEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
	// //	}
	//
	// // Rollback 되지 않기 때문에 after 이용하여 지워준다.
	// @AfterAll
	// public void afterDelete() {
	// 	performanceRepository.deleteAllInBatch();
	// 	planRepository.deleteAllInBatch();
	// }
}
