package com.order.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/30 21:09
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
	@Override
	protected Object determineCurrentLookupKey() {
		String dataSourceType = DynamicDataSourceContextHolder.getDataSourceType();
		if (null == dataSourceType) {
			return "master";
		}
		return dataSourceType;
	}
}
