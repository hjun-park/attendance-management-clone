package com.hjun.timereport.task;

import java.util.List;

import com.hjun.timereport.task.dto.TaskDto;
import com.hjun.timereport.task.entity.Task;

public interface TaskService {
	List<Task> getTasks();

	TaskDto getDefaultTaskDto();
}
