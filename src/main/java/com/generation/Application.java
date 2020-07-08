package com.generation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: czw
 * @CreateTime: 2020-07-04 14:26
 * @UpdeteTime: 2020-07-04 14:26
 * @Description:
 */
@SpringBootApplication
@ServletComponentScan
public class Application {
	public static final int nums=10;
	public static ApplicationContext applicationContext;
	private static long start;
	static long time = -1;

	public static void main(String[] args) throws SQLException {
		applicationContext = SpringApplication.run(Application.class, args);
		ExecutorService es = Executors.newFixedThreadPool(nums);
		start = System.currentTimeMillis();
		for (int i = 0; i < nums; i++)
			es.submit(new Task());

	}

	static class Task implements Runnable {
		DataUtil dataUtil = new DataUtil();

		@Override
		public void run() {
			for (int i = 0; i < 1000; i++) {
				try {
					dataUtil.operate(dataUtil.createDatas());
					System.out.println("task完成" + i + "次" + Thread.currentThread().getName());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//long end = System.currentTimeMillis();
			//synchronized (Application.class) {
			//	if (time < end) {
			//		time = end;
			//	}
			//}
		}
	}
}
