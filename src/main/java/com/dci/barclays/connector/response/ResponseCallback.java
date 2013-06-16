package com.dci.barclays.connector.response;

import org.apache.http.HttpResponse;

public interface ResponseCallback {

	public void excute(HttpResponse response);
}
