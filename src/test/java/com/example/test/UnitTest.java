package com.example.test;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.example.test.service.ValidationService;

class UnitTest {

	ValidationService service = new ValidationService();

	@Test
	void whenIsNullOrEmpty_isNull_thenReturnTrue() {
		Assert.assertEquals(service.isNullOrEmpty(null), true);
	}

	@Test
	void whenIsNullOrEmpty_isNotNullAndNotEmpty_thenReturnTrue() {
		Assert.assertEquals(service.isNullOrEmpty("Testing"), false);
	}

}
