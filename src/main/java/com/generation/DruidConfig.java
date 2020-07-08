package com.generation;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: czw
 * @CreateTime: 2020-07-04 10:46
 * @UpdeteTime: 2020-07-04 10:46
 * @Description:
 */
@Configuration
public class DruidConfig {

	@ConfigurationProperties("spring.datasource.druid")
	@Bean(initMethod = "init", value = "druidDataSource")
	public DruidDataSource druid() throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setProxyFilters(filters());
		return dataSource;
	}

	private List<Filter> filters() {
		List<Filter> list = new ArrayList<>();
		list.add(statFilter());
		return list;
	}

	/**
	 * 配置statFilter
	 * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_StatFilter
	 *
	 * @return
	 */
	@Bean(value = "stat-filter")
	public StatFilter statFilter() {
		StatFilter filter = new StatFilter();
		filter.setLogSlowSql(true); //慢sql统计
		filter.setSlowSqlMillis(0); //慢sql时间设置
		filter.setMergeSql(true);   //合并sql，将没有参数化的sql合并
		return filter;
	}


	/**
	 * 配置StatViewServlet配置
	 * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_StatViewServlet%E9%85%8D%E7%BD%AE
	 *
	 * @return
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		Map<String, String> initMap = new HashMap<>();
		initMap.put("resetEnable", "true"); //允许清空统计数据
		initMap.put("loginUsername", "druid");   //用户名
		initMap.put("loginPassword", "druid");   //密码
		//initMap.put("deny", "128.242.127.4"); //访问控制
		registrationBean.setInitParameters(initMap);    //设置初始参数
		return registrationBean;
	}

	/**
	 * 配置druid和spring关联监控
	 * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_Druid%E5%92%8CSpring%E5%85%B3%E8%81%94%E7%9B%91%E6%8E%A7%E9%85%8D%E7%BD%AE
	 *
	 * @return 为springAOP的intercept.MethodInterceptor的实现类，采用spring方式配置即可
	 * 通知
	 */
	@Bean(value = "druid-stat-interceptor")
	public DruidStatInterceptor interceptor() {
		return new DruidStatInterceptor();
	}

	/**
	 * @return 切点
	 */
	@Bean(value = "druid-stat-pointcut")
	public JdkRegexpMethodPointcut pointcut() {
		JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
		//正则匹配
		pointcut.setPatterns("com.generation.*");
		return pointcut;
	}

	/**
	 * @param interceptor
	 * @param pointcut
	 * @return 切点织入
	 */
	@Bean
	public DefaultPointcutAdvisor druidStatAdvisor(@Qualifier("druid-stat-interceptor") DruidStatInterceptor interceptor,
	                                               @Qualifier("druid-stat-pointcut") JdkRegexpMethodPointcut pointcut) {
		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setPointcut(pointcut);
		defaultPointcutAdvisor.setAdvice(interceptor);
		return defaultPointcutAdvisor;
	}

}
