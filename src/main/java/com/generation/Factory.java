package com.generation;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * @Author: czw
 * @CreateTime: 2020-07-03 11:38
 * @UpdeteTime: 2020-07-03 11:38
 * @Description:
 */
public class Factory {

	public static DataSource createDataSource() {
		return DataSourceHolder.dataSource;
	}

	private static class DataSourceHolder {
		static DataSource dataSource;

		static {
			File file = new File("src/main/resources/dataSource.properties");
			Properties properties = new Properties();
			try {
				FileReader fr = new FileReader(file);
				properties.load(fr);
				dataSource = DruidDataSourceFactory.createDataSource(properties);
				StatFilter statFilter = StatFilter.getStatFilter((DataSourceProxy) dataSource);
				statFilter.setLogSlowSql(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}
