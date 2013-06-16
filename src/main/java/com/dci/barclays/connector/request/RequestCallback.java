package com.dci.barclays.connector.request;

import org.apache.http.client.methods.HttpRequestBase;

public interface RequestCallback {

	public void excute(HttpRequestBase request);
}
