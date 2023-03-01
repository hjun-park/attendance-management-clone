package com.hjun.timereport.biweekly.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "HOLIDAY")
@SequenceGenerator(name = "HOLIDAY_SEQ_GENERATOR", sequenceName = "HOLIDAY_SEQ", initialValue = 1, allocationSize = 1)
public class Holiday {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERFORMANCE_SEQ_GENERATOR")
	@Column(name = "holiday_id")
	private Long holidayId;

	@Column(name = "holiday_date")
	private String holidayDate;

	@Column(name = "day_of_the_week")
	private Integer dayOfTheWeek;

	@Column(name = "week")
	private Integer week;

	@Column(name = "luna_day")
	private String lunaDay;

	@Column(name = "solar_name")
	private String solarName;

	@Column(name = "luna_name")
	private String lunaName;

	@Column(name = "is_holiday")
	private String isHoliday;

	@Column(name = "holiday_title")
	private String holidayTitle;

	public Holiday() {
	}

	@Builder
	public Holiday(Long holidayId, String holidayDate, Integer dayOfTheWeek, Integer week, String lunaDay,
			String solarName, String lunaName, String isHoliday, String holidayTitle) {
		this.holidayId = holidayId;
		this.holidayDate = holidayDate;
		this.dayOfTheWeek = dayOfTheWeek;
		this.week = week;
		this.lunaDay = lunaDay;
		this.solarName = solarName;
		this.lunaName = lunaName;
		this.isHoliday = isHoliday;
		this.holidayTitle = holidayTitle;
	}

}
