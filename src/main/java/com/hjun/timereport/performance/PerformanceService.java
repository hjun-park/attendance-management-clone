package com.hjun.timereport.performance;

import java.util.List;

import com.hjun.timereport.performance.dto.ApprovalReq;
import com.hjun.timereport.performance.dto.PerformanceDayDto;
import com.hjun.timereport.performance.dto.PerformanceDto;
import com.hjun.timereport.performance.dto.PerformanceNoEnterDto;
import com.hjun.timereport.performance.dto.PerformanceSaveEditReq;
import com.hjun.timereport.performance.entity.Performance;

public interface PerformanceService {

	// 실적 조회V1 (전체조회, 특정 주 실적조회)
	List<PerformanceDayDto> findPerformanceV1(String day, Long memberId);

	// 실적 조회V2 (전체조회, 특정 주 실적조회)
	List<PerformanceDto> findPerformanceV2(String day, Long memberId);

	// 실적 수정 ( == 저장, 확정 누를 시 status로 받음 )
	Long editPerformance(String day, List<PerformanceSaveEditReq> perfEditReqs, Long memberId);

	// 실적 확정 (단순 status값만 변경)
	Long confirmPerformance(String day, Long memberId) throws Exception;

	// 미입력자 전체 조회
	List<PerformanceNoEnterDto> findNoEnterPerformance(String year, String month);

	// 계획으로 부터 실적 저장
	Long savePerformancesFromPlan(List<PerformanceDto> performances, Long memberId);

	// 프로젝트가 포함된 실적 조회
	boolean findPerformanceWithProject(String projectCode);

	// 실적 삭제
	Long deletePerformances(List<Long> perfIds, Long memberId, String day);

	Long deletePerf(String day, Long memberId, List<Integer> seqs);

	// 승인 조회시 사용(두 날짜 사이의 seq = 1고 deptCode를 가진 실적 조회)
	List<Performance> findByPerfDayBetweenAndDeptCodeAndSeq(String startDate, String endDate, String deptCode);

	// 승인 상태 변경시 사용(두 날짜 사이의 deptCode를 가진 실적 모두 조회
	List<Performance> findByPerfDayBetweenAndMemberDeptCode(String startDate, String endDate, String deptCode);

	// 마감시 해당월 승인 여부 조회
	boolean findPerformanceSignStatusExists(String day);

	// 승인 상태 변경시 공통 코드
	List<Performance> perfDayList(ApprovalReq approvalReq, Long memberId);

	boolean isExistPerfDayBetweenBy(String startDay, String endDay, Long memberId);
}
