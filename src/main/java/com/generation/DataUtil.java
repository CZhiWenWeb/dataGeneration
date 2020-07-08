package com.generation;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: czw
 * @CreateTime: 2020-07-03 14:05
 * @UpdeteTime: 2020-07-03 14:05
 * @Description:
 */
public class DataUtil {
	private AtomicInteger integer = new AtomicInteger(1);
	private DruidDataSource dataSource;

	DataUtil() {
		this.dataSource = (DruidDataSource) Application.applicationContext.getBean("druidDataSource");
	}

	static final String createTable = "create table ";
	static final String leftB = "(";
	private Random random = new Random();
	static final String rightB = ")";
	static final String comma = ",";
	static final String space = " ";
	static final String dot = "'";

	public String createDatas() {
		String temp = "insert into big_data (name,age,email) values";
		String name = "name";
		String email = "email";
		StringBuilder sb = new StringBuilder();
		sb.append(temp);
		int num = 1000;
		for (int i = 0; i < num; i++) {
			sb.append(leftB);
			sb.append(dot).append(name).append(i).append(dot).append(comma);
			sb.append(random.nextInt()).append(comma);
			sb.append(dot).append(email).append(i).append(dot);
			sb.append(rightB);
			sb.append(comma);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public void operate(String sql) throws SQLException {
		Connection connection = dataSource.getConnection();
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.executeUpdate();
		ps.close();             //statement和connection要分别释放
		connection.close();
	}


	public void createTable(String name, Map<String, String> fields) throws SQLException {
		fields.put("ss", FieldType.INT);
		fields.put("kk", FieldType.DATA);
		StringBuilder sb = new StringBuilder();
		sb.append(createTable).append(name).append(leftB);
		for (String field : fields.keySet()) {
			sb.append(field).append(space).append(fields.get(field
			)).append(comma);
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(rightB);
		PreparedStatement ps = dataSource.getConnection().prepareStatement(sb.toString());
		ps.executeUpdate();
		ps.close();
	}
}
