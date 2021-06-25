package com.order.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/30 21:12
 */
public class DynamicDataSourceRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
	private final static Logger LOGGER = LogManager.getLogger(DynamicDataSourceRegistrar.class);

	private Environment environment;
	private Binder binder;
	private Map<String, DataSource> customerDataSources = new HashMap<>();
	private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

	static {
		aliases.addAliases("url", new String[]{"jdbc-url"});
		aliases.addAliases("username", new String[]{"user"});
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		Map defaultDataSourceProperties = binder.bind("spring.datasource.master", Map.class).get();
		String type = (String) defaultDataSourceProperties.get("type");
		Class<? extends DataSource> clazz = getDataSourceType(type);
		DataSource defaultDataSource = bind(clazz, defaultDataSourceProperties);
		String master = (String) defaultDataSourceProperties.get("key");
		DynamicDataSourceContextHolder.dataSourceIds.add(master);
		LOGGER.info("主数据源{}注册成功", master);

		List<Map> configs = binder.bind("spring.datasource.slaves", Bindable.listOf(Map.class)).get();
		for (int i = 0; i < configs.size(); ++i) {
			Map customerDataSourceProperties = configs.get(i);
			clazz = getDataSourceType((String) customerDataSourceProperties.get("type"));
			DataSource customerDataSource = bind(clazz, customerDataSourceProperties);
			String slave = (String) customerDataSourceProperties.get("key");
			customerDataSources.put(slave, customerDataSource);
			DynamicDataSourceContextHolder.dataSourceIds.add(slave);
			LOGGER.info("从数据源{}注册成功", slave);
		}

		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(DynamicRoutingDataSource.class);
		MutablePropertyValues mpv = beanDefinition.getPropertyValues();
		mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
		mpv.addPropertyValue("targetDataSources", customerDataSources);
		registry.registerBeanDefinition("datasource", beanDefinition);
		LOGGER.info("数据源注册成功");
	}

	private Class<? extends DataSource> getDataSourceType(String type) {
		Class<? extends DataSource> clazz;
		try {
			if (StringUtils.hasLength(type)) {
				clazz = (Class<? extends DataSource>) Class.forName(type);
			} else {
				clazz = DruidDataSource.class;
			}
			return clazz;
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("Failed to resolve class with type %s ", type));
		}
	}

	private void bind(DataSource result, Map properties) {
		ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
		Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
		binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
	}

	private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
		ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
		Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
		return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get();
	}

	private <T extends DataSource> T bind(Class<T> clazz, String sourcePath) {
		Map properties = binder.bind(sourcePath, Map.class).get();
		return bind(clazz, properties);
	}

	@Override
	public void setEnvironment(Environment environment) {
		LOGGER.info("开始注册数据源");
		this.environment = environment;
		binder = Binder.get(environment);
	}
}
