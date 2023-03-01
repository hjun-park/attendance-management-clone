package com.hjun.timereport.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hjun.timereport.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByGroupId(String groupId);

}
