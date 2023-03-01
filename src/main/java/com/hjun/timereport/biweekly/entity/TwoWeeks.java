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

import com.hjun.timereport.biweekly.dto.TwoWeeksDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "TWO_WEEKS")
@SequenceGenerator(name = "TWO_WEEKS_SEQ_GENERATOR", sequenceName = "TWO_WEEKS_SEQ", initialValue = 1, allocationSize = 1)
public class TwoWeeks {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TWO_WEEKS_SEQ_GENERATOR")
	@Column(name = "id")
	private Long id;

	private String year;

	private String month;

	private String fromdt;

	private String todt;

	private String yyyyww;

	private String content;

	private String yyyymmww;



	@Builder
	public TwoWeeks(String year, String month, String fromdt, String todt, String yyyyww, String content,
			String yyyymmww) {
		this.year = year;
		this.month = month;
		this.fromdt = fromdt;
		this.todt = todt;
		this.yyyyww = yyyyww;
		this.content = content;
		this.yyyymmww = yyyymmww;
	}

	public static TwoWeeksDto entityToDto(TwoWeeks tw) {
		return TwoWeeksDto.builder()
				.year(tw.getYear())
				.fromdt(tw.getFromdt())
				.content(tw.getContent())
				.build();
	}

	public TwoWeeks() {
	}



}
