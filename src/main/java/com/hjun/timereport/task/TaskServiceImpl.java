package com.hjun.timereport.task;

import static com.hjun.timereport.global.response.BaseResponseStatus.NOT_EXIST_TASK;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjun.timereport.global.exception.BaseException;
import com.hjun.timereport.task.dto.TaskDto;
import com.hjun.timereport.task.entity.Task;
import com.hjun.timereport.task.repository.TaskRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;
	private final static String defaultGroupId = "TR001";

	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public TaskDto getDefaultTaskDto() {
		List<Task> tasks = taskRepository.findByGroupId(defaultGroupId);

		if(tasks.isEmpty()) {
			throw new BaseException(NOT_EXIST_TASK);
		}

		Task firstTask = tasks.get(0);

		Task secondTask = new Task();
		try {
			secondTask = taskRepository.findByGroupId(firstTask.getCodeId()).get(0);
		} catch (Exception e) {
			throw new BaseException(NOT_EXIST_TASK);
		}


		return TaskDto.builder()
				.groupMainId(firstTask.getGroupId())
				.groupSubId(firstTask.getCodeId())
				.codeId(secondTask.getCodeId())
				.codeMainNm(firstTask.getCodeNm())
				.codeSubNm(secondTask.getCodeNm())
				.build();
	}

	@Override
	public List<Task> getTasks() {
		return taskRepository.findAll();
	}

}
