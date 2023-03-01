package com.hjun.timereport.performance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.entity.Performance;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
	@Query("select p "
			+ "from Performance p "
			+ "where 1 = 1"
			+ "and p.member.deptCode = :deptCode "
			+ "and p.perfDay between :startDate and :endDate "
			+ "and p.seq = 1")
	List<Performance> findByPerfDayBetweenAndDeptCodeAndSeq(
			@Param("startDate") String startDate,
			@Param("endDate") String endDate,
			@Param("deptCode") String deptCode);

	List<Performance> findBySignStatusInAndPerfDayStartsWithOrderByMemberAscPerfDayAsc(List<String> signStatus, String yearMonth);

	boolean existsByGroupMainId(String groupMainId);

	List<Performance> findByPerfDayBetweenAndMemberDeptCode(String startDate, String endDate, String deptCode);

	List<Performance> findByMemberAndPerfDayBetweenOrderByPerfDay(Member member, String startDate, String endDate);

	List<Performance> findByPerfDayAndMemberMemberIdOrderBySignStatus(String perfDay, Long memberId);

	List<Performance> findByMemberAndPerfDay(Member member, String perfDay);

	boolean existsByMemberMemberIdAndPerfDayBetween(Long memberId, String startDay, String endDay);

	long deleteByPerfDay(String perfDay);

	Optional<Performance> findFirstByPerfDayBetweenOrderBySignStatus(String startOfWeekday, String targetDay);

	List<Performance> findByPerfDayBetween(String startOfWeekday, String targetDay);

	List<Performance> findByPerfIdInAndMemberMemberIdAndPerfDay(List<Long> perfId, Long memberId, String perfDay);

	List<Performance> findByPerfDayAndMemberMemberIdAndSeqIn(String perfDay, Long memberId, List<Integer> seqs);

	List<Performance> findByPerfDayAndMemberMemberId(String targetDay, Long memberId);

	//bulk Update 할 시에는 변경 감지로 update 수정이 불가능, 따라서 아래와 같이 repo로 직접 bulk update 진행
	@Modifying(clearAutomatically = true) 	// flush, clear 모두 진행
	@Query("update Performance p set p.signStatus = '3' where p.perfId = :perfId")
	int approvalAllPerformances(@Param("perfId") Long perfId);

	int countByMemberMemberId(Long memberId);

	int countBySignStatusAndPerfDay(String signStatus, String perfDay);
}
