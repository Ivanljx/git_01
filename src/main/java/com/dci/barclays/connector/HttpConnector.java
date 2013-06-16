package com.dci.barclays.connector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dci.barclays.connector.request.CookieRequestCallback;
import com.dci.barclays.connector.response.CookieResponseCallback;
import com.dci.barclays.connector.response.ExcelResponseCallback;
import com.dci.barclays.utils.Utils;

public class HttpConnector {
	private static final Logger logger = LoggerFactory
			.getLogger(HttpConnector.class);
	private String loginPageUrl = "https://live.barcap.com/UAB/ct_logon_basic?CT_ORIG_URL=%2FBC%2Fdispatcher&ct_orig_uri=%2FBC%2Fdispatcher";
	private String logonUrl = "https://live.barcap.com/UAB/ct_logon";
	private String downLoadUrl = "https://live.barcap.com/IDX/INDICES/IWS?ext=.xls";
	private String user = "lewinsni";
	private String password = "dc1research";
	private String startdate = "06/13/2012";
	private String enddate = "06/13/2012";
	private String downloadDir = "c:/";
	private String path;
	private Map<String, String> cookies = new HashMap<String, String>();

	public HttpConnector() {
		String date = Utils.format(System.currentTimeMillis(), "MM/dd/yyyy");
		this.startdate = date;
		this.enddate = date;
		this.downloadDir="c:/";
	}

	protected boolean gotoLoginPage() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(loginPageUrl);
		HttpResponse response;
		try {
			response = httpClient.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if (200 != status) {
				logger.error("[op:gotoLoginPage,status:" + status
						+ ",error:error status]");
				return false;
			}
			new CookieResponseCallback(cookies).excute(response);
		} catch (ClientProtocolException e) {
			logger.error("[op:gotoLoginPage,error:" + e.getMessage() + "]", e);
			return false;
		} catch (IOException e) {
			logger.error("[op:gotoLoginPage,error:" + e.getMessage() + "]", e);
			return false;
		} finally {
			get.releaseConnection();

		}

		return true;
	}

	protected boolean logon() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost get = new HttpPost(logonUrl);

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("user", user));
		formparams.add(new BasicNameValuePair("password", password));
		formparams.add(new BasicNameValuePair("pageId", "basic"));
		formparams.add(new BasicNameValuePair("auth_mode", "basic"));
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			get.setEntity(entity);

		} catch (UnsupportedEncodingException e) {
			logger.error("[op:logon,error:" + e.getMessage() + "]", e);
			return false;
		}

		new CookieRequestCallback(cookies).excute(get);
		HttpResponse response;
		try {
			response = httpClient.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if (200 != status && 302 != status) {
				logger.error("[op:logon,status:" + status
						+ ",error:error status]");
				return false;
			}
			new CookieResponseCallback(cookies).excute(response);
		} catch (ClientProtocolException e) {
			logger.error("[op:logon,error:" + e.getMessage() + "]", e);
			return false;
		} catch (IOException e) {
			logger.error("[op:logon,error:" + e.getMessage() + "]", e);
			return false;
		} finally {
			get.releaseConnection();

		}

		return true;
	}

	protected boolean download() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost get = new HttpPost(downLoadUrl);

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("requestID", "TimeSeriesGroup"));
		formparams.add(new BasicNameValuePair("groupid", "Performance"));
		formparams.add(new BasicNameValuePair("owner", user));
		formparams.add(new BasicNameValuePair("viewid", "Current Returns"));
		formparams.add(new BasicNameValuePair("viewowner", "system"));
		formparams.add(new BasicNameValuePair("title", ""));
		formparams.add(new BasicNameValuePair("asofdate", "06/13/2013"));// plz
																			// check
																			// how
																			// to
																			// set
																			// this
																			// parameter
		formparams.add(new BasicNameValuePair("frequency", "d"));
		formparams.add(new BasicNameValuePair("currency", "NUL"));
		formparams.add(new BasicNameValuePair("returnstype", "0"));
		formparams.add(new BasicNameValuePair("showAll", "1"));
		formparams.add(new BasicNameValuePair("dateFormat", "MM/dd/yyyy"));
		formparams.add(new BasicNameValuePair("period", "1y"));
		formparams.add(new BasicNameValuePair("startdate", startdate));
		formparams.add(new BasicNameValuePair("enddate", enddate));

		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			get.setEntity(entity);

		} catch (UnsupportedEncodingException e) {
			logger.error("[op:download,error:" + e.getMessage() + "]", e);
			return false;
		}

		new CookieRequestCallback(cookies).excute(get);
		HttpResponse response;
		try {
			response = httpClient.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if (200 != status && 302 != status) {
				logger.error("[op:download,status:" + status
						+ ",error:error status]");
				return false;
			}
			ExcelResponseCallback cb = new ExcelResponseCallback(downloadDir);
			cb.excute(response);
			String path = cb.getPath();
			if (null == path)
				return false;

			else {
				this.path=cb.getPath();
				logger.error("[op:download,path:" + path
						+ ",info:down load successfully]");
				return true;
			}
		} catch (ClientProtocolException e) {
			logger.error("[op:download,error:" + e.getMessage() + "]", e);
			return false;
		} catch (IOException e) {
			logger.error("[op:download,error:" + e.getMessage() + "]", e);
			return false;
		} finally {
			get.releaseConnection();

		}

	}

	public boolean excute() {
		boolean r = gotoLoginPage();
		if (r)
			r = logon();
		if (r)
			r = download();
		return r;

	}

	public void status() {
		logger.info(cookies.toString());
	}

	public String getStartdate() {
		return startdate;
	}

	public String getPath() {
		return path;
	}

	public void setLoginPageUrl(String loginPageUrl) {
		this.loginPageUrl = loginPageUrl;
	}

	public void setLogonUrl(String logonUrl) {
		this.logonUrl = logonUrl;
	}

	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

 	public static void main(String args[]) {
		
		logger.info("asd");
		HttpConnector c = new HttpConnector();
		c.gotoLoginPage();
		c.status();
		c.logon();
		c.status();
		c.download();
	}

}
