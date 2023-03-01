package com.hjun.timereport.task.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskDto {

	private String groupMainId;
	private String groupSubId;
	private String codeId;
	private String codeMainNm;
	private String codeSubNm;

	@Builder
	public TaskDto(String groupMainId, String groupSubId, String codeId, String codeMainNm, String codeSubNm) {
		this.groupMainId = groupMainId;
		this.groupSubId = groupSubId;
		this.codeId = codeId;
		this.codeMainNm = codeMainNm;
		this.codeSubNm = codeSubNm;
	}

}
