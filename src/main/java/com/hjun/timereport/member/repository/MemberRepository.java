package com.hjun.timereport.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hjun.timereport.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

	Optional<Member> findByMemberCode(String memberCode);

	List<Member> findAllByDeptCode(String deptCode);

	Optional<List<Member>> findMembersByDeptCode(String deptCode);

}
