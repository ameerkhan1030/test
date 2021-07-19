package com.example.test.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return ContextHolder.getDatabase();
	}
}