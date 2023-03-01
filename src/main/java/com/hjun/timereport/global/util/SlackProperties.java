package com.hjun.timereport.global.util;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;

@Getter
@Validated
@ConfigurationProperties(prefix = "slack")
@ConstructorBinding
public class SlackProperties {

	@NotBlank
	private final String authPrefix;

	@NotBlank
	private final String headerString;

	@NotBlank
	private final String botOauthToken;

	@NotBlank
	private final String url;

	public SlackProperties(@NotBlank String authPrefix, @NotBlank String headerString, @NotBlank String botOauthToken,
			@NotBlank String url) {
		this.authPrefix = authPrefix;
		this.headerString = headerString;
		this.botOauthToken = botOauthToken;
		this.url = url;
	}

}

