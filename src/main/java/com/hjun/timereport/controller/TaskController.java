package com.hjun.timereport.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hjun.timereport.global.response.BaseResponse;
import com.hjun.timereport.task.TaskService;
import com.hjun.timereport.task.entity.Task;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	public BaseResponse<List<Task>> readAllTasks() {
		return new BaseResponse<>(taskService.getTasks());
	}
}
