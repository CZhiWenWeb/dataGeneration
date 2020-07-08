package com.generation;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: czw
 * @CreateTime: 2020-07-04 10:05
 * @UpdeteTime: 2020-07-04 10:05
 * @Description: Druid 的监控数据可以在开启 StatFilter 后通过 DruidStatManagerFacade
 * 进行获取，获取到监控数据之后你可以将其暴露给你的监控系统进行使用。Druid
 * 默认的监控系统数据也来源于此。
 */
@RestController
public class DruidStatController {

	@RequestMapping("/druid/stat")
	public Object druidStat() {
		return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
	}
}
