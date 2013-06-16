package com.dci.barclays.connector.response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dci.barclays.utils.Utils;

public class ExcelResponseCallback implements ResponseCallback {
	private static final Logger logger = LoggerFactory
			.getLogger(ExcelResponseCallback.class);
	private String dir;
	private String path;

	public ExcelResponseCallback(String dir) {
		super();
		this.dir = dir;
	}

	public void excute(HttpResponse response) {

		try {
			InputStream inputStream = response.getEntity().getContent();
			long now = System.currentTimeMillis();
			path = dir + Utils.format(now) + "_" + now + ".xls";
			FileOutputStream outputStream = new FileOutputStream(new File(path));

			byte b[] = new byte[1024];
			int j = 0;
			while ((j = inputStream.read(b)) != -1) {
				outputStream.write(b, 0, j);
			}
			outputStream.flush();
			outputStream.close();
		} catch (IllegalStateException e) {
			logger.error("[op:excute,error:" + e.getMessage() + "]", e);
			path = null;
		} catch (IOException e) {
			logger.error("[op:excute,error:" + e.getMessage() + "]", e);
			path = null;

		}
	}

	public String getPath() {
		return path;
	}

}
