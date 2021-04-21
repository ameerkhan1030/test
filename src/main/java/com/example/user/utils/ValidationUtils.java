package com.example.user.utils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class ValidationUtils.
 */
@Slf4j
public class ValidationUtils {

	private ValidationUtils() {}
	
	private static final Pattern EMAIL_PATTERN = Pattern
			.compile("^[\\._A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	
	
	/**
	 * Validate email.
	 *
	 * @param email the email
	 * @return true, if successful
	 */
	public static boolean validateEmail(final String email) {
		boolean status;
		if (email != null && email.trim().length() <= 50) {
			Matcher matcher = EMAIL_PATTERN.matcher(email);
			if (matcher.matches()) {
				status = true;
			} else {
				status = false;
			}
		} else {
			status = false;
		}
		return status;
	}

	/**
	 * Checks if is empty string.
	 * 
	 * @param text the text
	 * @return true, if is empty string
	 */
	public static boolean isEmptyString(final String text) {
		return text == null || text.trim().length() == 0;
	}

	/**
	 * this method is to check given value is null or less than 0
	 * 
	 * @param value
	 * @return true, if given value is null or less than 0 otherwise, false
	 */
	public static boolean isNullValue(final Number value) {
		return Objects.isNull(value) || value.intValue() < 0;
	}

}
