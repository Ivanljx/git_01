package com.dci.barclays.connector.response;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieResponseCallback implements ResponseCallback {
	private static final Logger logger = LoggerFactory
			.getLogger(CookieResponseCallback.class);
	private Map<String, String> map = new HashMap<String, String>();

	public CookieResponseCallback(Map<String, String> map) {
		super();
		this.map = map;
	}

	public void excute(HttpResponse response) {
		Header[] headers = response.getHeaders("Set-Cookie");
		if (null == headers || headers.length <= 0)
			return;
		for (Header header : headers) {
			String value = header.getValue();
			String ck = value.split(";")[0];
			int p = ck.indexOf("=");
			if (p < 0) {
				continue;
			}
			String k = ck.substring(0, p);
			String v = ck.substring(p + 1);
			map.put(k, v);
		}
	}

}
