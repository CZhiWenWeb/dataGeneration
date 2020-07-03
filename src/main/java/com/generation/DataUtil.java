package com.generation;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: czw
 * @CreateTime: 2020-07-03 14:05
 * @UpdeteTime: 2020-07-03 14:05
 * @Description:
 */
public class DataUtil {
	static final String createTable = "create table ?";
	static final String leftB = "(";
	static final String rightB = ")";
	static final String comma = ",";
	static final String space = " ";

	public static void createTable(String name, Map<String, String> fields) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append(createTable).append(leftB);
		for (String field : fields.keySet()) {
			sb.append(field).append(space).append(fields.get(field
			)).append(comma);
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(rightB);
		PreparedStatement ps = getPs(sb.toString());
		ps.setString(1, name);
		ps.executeUpdate();
		ps.close();
	}

	private static PreparedStatement getPs(String sql) throws SQLException {
		DataSource dataSource = Factory.createDataSource();
		return dataSource.getConnection().prepareStatement(sql);
	}


	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("ss", FieldType.INT);
		map.put("kk", FieldType.DATA);
		createTable("dddd", map);
	}
}
