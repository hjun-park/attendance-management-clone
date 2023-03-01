package com.hjun.timereport.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hjun.timereport.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
