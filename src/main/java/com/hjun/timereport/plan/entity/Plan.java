package com.hjun.timereport.plan.entity;

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
import com.hjun.timereport.plan.dto.PlanSaveReq;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "PLAN")
@SequenceGenerator(name = "PLAN_SEQ_GENERATOR",
					sequenceName = "PLAN_SEQ", initialValue = 1, allocationSize = 1)
public class Plan extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLAN_SEQ_GENERATOR")
	@Column(name = "plan_id")
	private Long planId;

	@Column(name = "seq")
	private int seq;

	@Column(name = "task_hour")
	private Double taskHour;

	@Column(name = "plan_day")
	private String planDay;

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

	@Column(name = "enroll_yn")
	private String enrollYn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;


	public Plan() {
	}

	public void editPlan(PlanSaveReq psr, String enrollYn) {
		this.startedHour = psr.getStartedHour();
		this.endedHour = psr.getEndedHour();
		this.groupMainId = psr.getGroupMainId();
		this.groupSubId = psr.getGroupSubId();
		this.codeId = psr.getCodeId();
		this.codeMainNm = psr.getCodeMainNm();
		this.codeSubNm = psr.getCodeSubNm();

		this.workDetail = psr.getWorkDetail();
		this.seq = psr.getSeq();
		this.taskHour = psr.getTaskHour();

		this.wfhYn = psr.getWfhYn();
		this.enrollYn = enrollYn;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Builder
	public Plan(int seq, Double taskHour, String planDay, Double dayHour, String startedHour,
			String endedHour, String groupMainId, String groupSubId, String codeId, String codeMainNm, String codeSubNm,
			String workDetail, String wfhYn, String enrollYn, Member member) {
		this.seq = seq;
		this.taskHour = taskHour;
		this.planDay = planDay;
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
		this.enrollYn = enrollYn;
		this.member = member;
	}

}
