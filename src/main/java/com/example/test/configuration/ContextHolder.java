package com.example.test.configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextHolder {
	
	private ContextHolder() {
	}

	private static ThreadLocal<Database> context = new ThreadLocal<>();

	public static void set(Database database) {
		context.set(database);
	}

	public static Database getDatabase() {
		return context.get();
	}
	public static void clear() {
		context.remove();
	}
}