package com.order.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/30 21:11
 */
public class DynamicDataSourceContextHolder {
	private static final ThreadLocal<String> HOLDER = new ThreadLocal<String>();
	protected static List<String> dataSourceIds = new ArrayList<>();

	public static void setDataSourceType(String dataSourceType) {
		HOLDER.set(dataSourceType);
	}

	public static String getDataSourceType() {
		return HOLDER.get();
	}

	public static void clearDataSourceType() {
		HOLDER.remove();
	}

	public static boolean containsDataSource(String dataSourceId) {
		return dataSourceIds.contains(dataSourceId);
	}
}
