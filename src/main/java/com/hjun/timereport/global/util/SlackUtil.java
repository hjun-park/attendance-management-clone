package com.hjun.timereport.global.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Component;

@Component
public class SlackUtil {

	private final SlackProperties sp;

	public SlackUtil(SlackProperties sp) {
		this.sp = sp;
	}

	public void sendSlack(Double weeklyWorkHour) throws Exception  {
		String message = "현재 주 근무시간이 "+ weeklyWorkHour +"시간을 초과하였습니다. 금일 이후 계획을 수정해주시길 바랍니다.";
		String urlStr = sp.getUrl() + URLEncoder.encode(message, "UTF-8");

		HttpURLConnection conn = null;
		URL url = new URL(urlStr);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestMethod("GET");
		String authorization = sp.getAuthPrefix() + sp.getBotOauthToken();
		conn.setRequestProperty(sp.getHeaderString(), authorization);

		conn.connect();
		new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	}
}
