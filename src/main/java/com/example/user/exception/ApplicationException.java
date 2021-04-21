package com.example.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class ApplicationException.
 */
@Setter
@Getter
@AllArgsConstructor
public class ApplicationException extends Exception{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private final long status;
	
	/** The status. */
	private final String message;
	
	/** The message. */
	private final String error;
	
	public ApplicationException() {
		this.status = 0;
		this.message = "";
		this.error = "";
		
	}
	
}
