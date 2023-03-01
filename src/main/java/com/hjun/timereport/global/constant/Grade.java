package com.hjun.timereport.global.constant;

import lombok.Getter;

@Getter
public enum Grade {

	USER(1), MANAGER(2), ADMIN(3);

	private final int value;

	Grade(int value) {
		this.value = value;
	}

}
