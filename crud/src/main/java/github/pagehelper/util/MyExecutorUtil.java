package github.pagehelper.util;

import com.github.pagehelper.Dialect;
import com.github.pagehelper.PageException;
import com.yxt.crud.utils.PatternUtils;
import com.yxt.crud.utils.RedisUtils;
import com.yxt.crud.utils.SpringContextHolder;
import com.yxt.crud.utils.Utils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/08/27 18:20
 */
@Component
public class MyExecutorUtil {

	private static Field additionalParametersField;

	static {
		try {
			additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
			additionalParametersField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new PageException("获取 BoundSql 属性 additionalParameters 失败: " + e, e);
		}
	}

	static {

	}

	/**
	 * 获取 BoundSql 属性值 additionalParameters
	 *
	 * @param boundSql
	 * @return
	 */
	public static Map<String, Object> getAdditionalParameter(BoundSql boundSql) {
		try {
			return (Map<String, Object>) additionalParametersField.get(boundSql);
		} catch (IllegalAccessException e) {
			throw new PageException("获取 BoundSql 属性值 additionalParameters 失败: " + e, e);
		}
	}

	/**
	 * 尝试获取已经存在的在 MS，提供对手写count和page的支持
	 *
	 * @param configuration
	 * @param msId
	 * @return
	 */
	public static MappedStatement getExistedMappedStatement(Configuration configuration, String msId) {
		MappedStatement mappedStatement = null;
		try {
			mappedStatement = configuration.getMappedStatement(msId, false);
		} catch (Throwable t) {
			//ignore
		}
		return mappedStatement;
	}

	/**
	 * 执行手动设置的 count 查询，该查询支持的参数必须和被分页的方法相同
	 *
	 * @param executor
	 * @param countMs
	 * @param parameter
	 * @param boundSql
	 * @param resultHandler
	 * @return
	 * @throws SQLException
	 */
	public static Long executeManualCount(Executor executor, MappedStatement countMs,
										  Object parameter, BoundSql boundSql,
										  ResultHandler resultHandler) throws SQLException {
		long l = System.currentTimeMillis();
		CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
		BoundSql countBoundSql = countMs.getBoundSql(parameter);
		Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
		Long count = ((Number) ((List) countResultList).get(0)).longValue();
		System.out.println("Count(*) : " + (System.currentTimeMillis() - l));
		return count;
	}

	/**
	 * 执行自动生成的 count 查询
	 *
	 * @param dialect
	 * @param executor
	 * @param countMs
	 * @param parameter
	 * @param boundSql
	 * @param rowBounds
	 * @param resultHandler
	 * @return
	 * @throws SQLException
	 */
	public static Long executeAutoCount(Dialect dialect, Executor executor, MappedStatement countMs,
										Object parameter, BoundSql boundSql,
										RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
		long l = System.currentTimeMillis();
		Long count;
		RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);
		Map<String, Object> additionalParameters = getAdditionalParameter(boundSql);
		//创建 count 查询的缓存 key
		CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
		String countHashKey = null;
		Integer countRedis = null;
		if (parameter instanceof Map) {
			Map param = (Map) parameter;
			Map map = new HashMap();
			map.putAll(param);
			map.remove("pageNum");
			map.remove("pageSize");
			List<String> tableNames = PatternUtils.getPossibleTableNames(boundSql.getSql(), "select");
			if (!CollectionUtils.isEmpty(tableNames)) {
				countHashKey = "count:" + tableNames.get(0) + ":" + Utils.getMd5(countKey.toString() + map.toString());
				countRedis = (Integer) redisUtils.get(countHashKey);
			}
		}

		if (countRedis == null) {
			//调用方言获取 count sql
			String countSql = dialect.getCountSql(countMs, boundSql, parameter, rowBounds, countKey);
			//countKey.update(countSql);
			BoundSql countBoundSql = new BoundSql(countMs.getConfiguration(), countSql, boundSql.getParameterMappings(), parameter);
			//当使用动态 SQL 时，可能会产生临时的参数，这些参数需要手动设置到新的 BoundSql 中
			for (String key : additionalParameters.keySet()) {
				countBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
			}
			//执行 count 查询
			Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
			count = (Long) ((List) countResultList).get(0);
			if (countHashKey != null) {
				redisUtils.set(countHashKey, count,  24 * 60 * 60);
			}
		} else {
			count = countRedis.longValue();
		}

		System.out.println(String.format("Count(*) consumed time : %s", (System.currentTimeMillis() - l)));
		return count;
	}

	/**
	 * 分页查询
	 *
	 * @param dialect
	 * @param executor
	 * @param ms
	 * @param parameter
	 * @param rowBounds
	 * @param resultHandler
	 * @param boundSql
	 * @param cacheKey
	 * @param <E>
	 * @return
	 * @throws SQLException
	 */
	public static  <E> List<E> pageQuery(Dialect dialect, Executor executor, MappedStatement ms, Object parameter,
										 RowBounds rowBounds, ResultHandler resultHandler,
										 BoundSql boundSql, CacheKey cacheKey) throws SQLException {
		//判断是否需要进行分页查询
		if (dialect.beforePage(ms, parameter, rowBounds)) {
			//生成分页的缓存 key
			CacheKey pageKey = cacheKey;
			//处理参数对象
			parameter = dialect.processParameterObject(ms, parameter, boundSql, pageKey);
			//调用方言获取分页 sql
			String pageSql = dialect.getPageSql(ms, boundSql, parameter, rowBounds, pageKey);
			BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameter);

			Map<String, Object> additionalParameters = getAdditionalParameter(boundSql);
			//设置动态参数
			for (String key : additionalParameters.keySet()) {
				pageBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
			}
			//执行分页查询
			return executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, pageKey, pageBoundSql);
		} else {
			//不执行分页的情况下，也不执行内存分页
			return executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, boundSql);
		}
	}

}

