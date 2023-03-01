package com.hjun.timereport.performance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hjun.timereport.global.entity.BaseEntity;
import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.dto.PerformanceDto;
import com.hjun.timereport.performance.dto.PerformanceSaveEditReq;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "PERFORMANCE")
@SequenceGenerator(name = "PERFORMANCE_SEQ_GENERATOR", sequenceName = "PERFORMANCE_SEQ", initialValue = 1, allocationSize = 1)
public class Performance extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERFORMANCE_SEQ_GENERATOR")
	@Column(name = "perf_id")
	private Long perfId;

	@Column(name = "seq")
	private int seq;

	@Column(name = "task_hour")
	private Double taskHour;

	@Column(name = "perf_day")
	private String perfDay;

	@Column(name = "day_hour")
	private Double dayHour;

	@Column(name = "started_hour")
	private String startedHour;

	@Column(name = "ended_hour")
	private String endedHour;

	@Column(name = "group_main_id")
	private String groupMainId;

	@Column(name = "group_sub_id")
	private String groupSubId;

	@Column(name = "code_id")
	private String codeId;

	@Column(name = "code_main_nm")
	private String codeMainNm;

	@Column(name = "code_sub_nm")
	private String codeSubNm;

	@Column(name = "detail")
	private String workDetail;

	@Column(name = "wfh_yn")
	private String wfhYn;

	@Column(name = "sign_status")
	private String signStatus;

	@Column(name = "break_time")
	private Double breakTime;

	@Column(name = "overtime_detail")
	private String overtimeDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;


	public Performance() {
	}

	@Builder
	public Performance(int seq, Double taskHour, String perfDay, Double dayHour, String startedHour,
			String endedHour, String groupMainId, String groupSubId, String codeId, String codeMainNm, String codeSubNm,
			String workDetail, String wfhYn, String signStatus, Double breakTime, String overtimeDetail, Member member) {
		this.seq = seq;
		this.taskHour = taskHour;
		this.perfDay = perfDay;
		this.dayHour = dayHour;
		this.startedHour = startedHour;
		this.endedHour = endedHour;
		this.groupMainId = groupMainId;
		this.groupSubId = groupSubId;
		this.codeId = codeId;
		this.codeMainNm = codeMainNm;
		this.codeSubNm = codeSubNm;
		this.workDetail = workDetail;
		this.wfhYn = wfhYn;
		this.signStatus = signStatus;
		this.breakTime = breakTime;
		this.overtimeDetail = overtimeDetail;
		this.member = member;
	}

	public static Performance dtoToEntity(PerformanceDto p, Member member) {
		return Performance.builder()
				.seq(p.getSeq())
				.taskHour(p.getTaskHour())
				.perfDay(String.valueOf(p.getPerfDay()))
				.dayHour(p.getDayHour())
				.startedHour(p.getStartedHour())
				.endedHour(p.getEndedHour())
				.groupMainId(p.getGroupMainId())
				.groupSubId(p.getGroupSubId())
				.codeId(p.getCodeId())
				.codeMainNm(p.getCodeMainNm())
				.codeSubNm(p.getCodeSubNm())
				.workDetail(p.getWorkDetail())
				.signStatus(p.getSignStatus())
				.breakTime(p.getBreakTime())
				.overtimeDetail(p.getOvertimeDetail())
				.wfhYn(p.getWfhYn())
				.member(member)
				.build();
	}

	public void editPerformance(PerformanceSaveEditReq p) {
		this.seq = p.getSeq();
		this.taskHour = p.getTaskHour();
		this.dayHour = p.getDayHour();
		this.startedHour = p.getStartedHour();
		this.endedHour = p.getEndedHour();
		this.groupMainId = p.getGroupMainId();
		this.groupSubId = p.getGroupSubId();
		this.codeId = p.getCodeId();
		this.codeMainNm = p.getCodeMainNm();
		this.codeSubNm = p.getCodeSubNm();
		this.workDetail = p.getWorkDetail();
		this.signStatus = String.valueOf(p.getSignStatus());
		this.breakTime = p.getBreakTime();
		this.overtimeDetail = p.getOvertimeDetail();
		this.wfhYn = p.getWfhYn();
	}

	public void setSignStatusToConfirm() {
		this.signStatus = "2";
	}

	public void setSignStatusToApproval() {
		this.signStatus = "3";
	}

	public void updateSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}




}
