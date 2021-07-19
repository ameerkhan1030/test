package com.example.test.service;

public class ValidationService {

	public Boolean isNullOrEmpty(final String value) {

		if (value == null || value.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
