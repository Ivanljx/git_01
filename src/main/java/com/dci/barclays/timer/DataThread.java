package com.dci.barclays.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dci.barclays.connector.HttpConnector;
import com.dci.barclays.utils.Utils;

public class DataThread extends Thread {
	private static final Logger logger = LoggerFactory
			.getLogger(DataThread.class);
	public void debugHttp(){
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"debug");
	}
	public void run() {
		HttpConnector hc = new HttpConnector();
		hc.setLoginPageUrl("https://live.barcap.com/UAB/ct_logon_basic?CT_ORIG_URL=%2FBC%2Fdispatcher&ct_orig_uri=%2FBC%2Fdispatcher");
		hc.setLogonUrl("https://live.barcap.com/UAB/ct_logon");
		hc.setDownLoadUrl("https://live.barcap.com/IDX/INDICES/IWS?ext=.xls");
		hc.setUser("lewinsni");
		hc.setPassword("dc1research");
		long now = System.currentTimeMillis();
		String date = Utils.format(now - 24 * 3600 * 1000l, "MM/dd/yyyy");
		hc.setStartdate(date);
		hc.setEnddate(date);
		hc.setDownloadDir("c:/");
		if (hc.excute()) {
			logger.info(hc.getPath());
		}

	}
	
	public static void main(String atg[]){
		DataThread dt=new DataThread();
		//dt.debugHttp();
		dt.run();
	}

}
