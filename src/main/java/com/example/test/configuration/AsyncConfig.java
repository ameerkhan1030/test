package com.example.test.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setTaskDecorator(new MdcTaskDecorator());
		return executor;
	}

	class MdcTaskDecorator implements TaskDecorator {

		@Override
		public Runnable decorate(Runnable runnable) {
			Map<String, String> contextMap;
			if (MDC.getCopyOfContextMap() == null) {
				contextMap = new HashMap<>();
			} else {
				contextMap = MDC.getCopyOfContextMap();
			}

			return () -> {
				try {
					MDC.setContextMap(contextMap);
					runnable.run();
				} finally {
					MDC.clear();
				}
			};
		}
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (throwable, method, obj) -> log.error(String.format("async error in method %s, error msg is %s", method.getName(),
					throwable.getMessage()), throwable);
	}
}
