package com.hjun.timereport.performance.dto;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoEnterPerfDayOfMemeberDto {
	private Long memberId;
	private String perfDay;

	@Builder
	public NoEnterPerfDayOfMemeberDto(Long memberId, String perfDay) {
		this.memberId = memberId;
		this.perfDay = perfDay;
	}

	@Override
	public int hashCode() {
		return Objects.hash(memberId, perfDay);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoEnterPerfDayOfMemeberDto other = (NoEnterPerfDayOfMemeberDto) obj;
		return Objects.equals(memberId, other.memberId) && Objects.equals(perfDay, other.perfDay);
	}


}
