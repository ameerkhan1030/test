package com.example.user.view;

import java.io.Serializable;

import lombok.Data;

/**
 * The Class ResponseModel.
 */
@Data
public class ResponseModel implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The status. */
	private long status;
	
	/** Error. */
	private String error;
	
	/** The message. */
	private String message = "";
	
	/** The payload. */
	private Object payload;
	
}
