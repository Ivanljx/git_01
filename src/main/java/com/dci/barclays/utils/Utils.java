package com.dci.barclays.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String format(Long t) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date(t));
	}
	public static String format(Long t,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(t));
	}
	
	
	public static void main(String args[]){
		System.out.println(format(System.currentTimeMillis(),"MM/dd/yyyy"));
	}
}
