package com.dci.barclays.connector.request;

import java.util.Map;
import org.apache.http.client.methods.HttpRequestBase;

public class CookieRequestCallback implements RequestCallback {
	private Map<String, String> cookies;

	public CookieRequestCallback(Map<String, String> cookies) {
		super();
		this.cookies = cookies;
	}

	public void excute(HttpRequestBase request) {
		String v = append();
		if (null != v) {
			request.addHeader("Cookie", v);
		}
	}

	private String append() {
		if (null == cookies || cookies.size() <= 0)
			return null;
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, String> entry : cookies.entrySet()) {
			i++;
			if (i > 1)
				builder.append("; ");
			builder.append(entry.getKey()).append("=").append(entry.getValue());
		}
		return builder.toString();
	}

}
