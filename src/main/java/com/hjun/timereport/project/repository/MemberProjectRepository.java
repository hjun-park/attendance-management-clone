package com.hjun.timereport.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.project.entity.MemberProject;
import com.hjun.timereport.project.entity.Project;

public interface MemberProjectRepository extends JpaRepository<MemberProject, Long> {

	List<MemberProject> findByMember(Member member);

	List<MemberProject> findByProject(Project project);

	Optional<MemberProject> findByMemberAndProject(Member member, Project project);

	Optional<MemberProject> findByMemberMemberIdAndProjectProjectId(Long memberId, Long projectId);
}
