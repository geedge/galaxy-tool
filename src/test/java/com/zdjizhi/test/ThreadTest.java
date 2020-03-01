package com.zdjizhi.test;

import java.io.IOException;

import com.zdjizhi.utils.FormatUtils;

public class ThreadTest implements Runnable {
	    @Override
	    public void run() {
	        try {
			FormatUtils build = new FormatUtils.Builder(false).build();
//			build.getSnowflakeId(workerId, dataCenterId);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    public static void main(String[] args) throws IOException {
			try {
				ThreadTest a = new ThreadTest();
		        Thread a1 = new Thread(a);
		        Thread b1 = new Thread(a);
		        a1.start();
//		        a1.join();
		        b1.start();
//		        b1.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}