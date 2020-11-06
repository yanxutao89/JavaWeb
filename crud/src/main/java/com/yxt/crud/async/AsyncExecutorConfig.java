package com.yxt.crud.async;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @Author: Yanxt7
 * @Desc: 线程池配置
 * @Date: 2020/01/19 10:36
 */
@EnableAsync
@Configuration
@EnableScheduling
public class AsyncExecutorConfig implements SchedulingConfigurer, AsyncConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncExecutorConfig.class);

	private static final int NUM_CPU = Runtime.getRuntime().availableProcessors();
	private static final int POOL_SIZE = NUM_CPU * 2;
	private static final int MAX_POOL_SIZE = NUM_CPU * 4;
	private static final int QUEUE_CAPACITY = NUM_CPU * 10;
	private static final int AWAIT_TIME = 60;

	/**
	 * 异步任务执行线程池
	 * @return
	 */
	@Bean(name = "asyncExecutor", destroyMethod = "shutdown")
	public ThreadPoolTaskExecutor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(POOL_SIZE);
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setKeepAliveSeconds(AWAIT_TIME);
		executor.setThreadNamePrefix("asyncExecutor-");
		executor.setRejectedExecutionHandler(new NewThreadRunsPolicy("asyncExecutor-temp-"));
		executor.initialize();
		return executor;
	}

	/**
	 * 定时任务执行线程池
	 * @return
	 */
	@Bean(name = "taskScheduler", destroyMethod = "shutdown")
	public ThreadPoolTaskScheduler taskScheduler(){
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(POOL_SIZE);
		scheduler.setAwaitTerminationSeconds(AWAIT_TIME);
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		scheduler.setDaemon(true); // 设置为守护线程
		scheduler.setThreadNamePrefix("taskScheduler-");
		scheduler.setRejectedExecutionHandler(new NewThreadRunsPolicy("taskScheduler-temp-"));
		scheduler.initialize();
		return scheduler;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		ThreadPoolTaskScheduler taskScheduler = taskScheduler();
		scheduledTaskRegistrar.setTaskScheduler(taskScheduler);
	}

	@Override
	public Executor getAsyncExecutor() {
		return asyncExecutor();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				LOGGER.error(String.format("异步任务执行异常,异常方法:%s,异常原因:%s", method.getName(), ex.getMessage()));
				ex.printStackTrace();
			}
		};
	}

	/**
	 * 拒绝时开启全新线程
	 */
	private static final class NewThreadRunsPolicy implements RejectedExecutionHandler {

		private String threadNamePrefix;

		public NewThreadRunsPolicy(String threadNamePrefix) {
			super();
			this.threadNamePrefix = threadNamePrefix;
		}

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			if (!executor.isShutdown()) {
				try {
					final Thread thread = new Thread(r, threadNamePrefix);
					thread.start();
				} catch (Throwable e) {
					throw new RejectedExecutionException("Failed to start a new thread", e);
				}
			}
		}
	}

	/**
	 * 拒绝时延后执行任务
	 */
	private static final class PostponedRunsPolicy implements RejectedExecutionHandler {

		private String threadNamePrefix;

		public PostponedRunsPolicy(String threadNamePrefix) {
			super();
			this.threadNamePrefix = threadNamePrefix;
		}

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			if (!executor.isShutdown()) {
				try {
					executor.getQueue().offer(r, AWAIT_TIME, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					throw new RejectedExecutionException("Interrupted waiting for worker");
				}
				throw new RejectedExecutionException("Timed out while attempting to enqueue task");
			}
		}
	}
}
