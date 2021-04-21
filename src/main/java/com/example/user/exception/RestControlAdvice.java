package com.example.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.user.view.ResponseModel;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class RestControlAdvice {

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ResponseModel> applicationException(ApplicationException applicationException) {

		ResponseEntity<ResponseModel> entity = null;
		ResponseModel model = new ResponseModel();
		model.setStatus(applicationException.getStatus());
		model.setMessage(applicationException.getMessage());
		log.warn(applicationException.getMessage());
		if (applicationException.getStatus() == 4001) {
			entity = new ResponseEntity<>(model, HttpStatus.UNAUTHORIZED);
		}
		if (applicationException.getStatus() == 4003) {
			entity = new ResponseEntity<>(model, HttpStatus.FORBIDDEN);
		}
		if (applicationException.getStatus() == 4009) {
			entity = new ResponseEntity<>(model, HttpStatus.CONFLICT);
		}
		if (applicationException.getStatus() == 4012) {
			entity = new ResponseEntity<>(model, HttpStatus.PRECONDITION_FAILED);
		}
		if (applicationException.getStatus() == 4022) {
			entity = new ResponseEntity<>(model, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (applicationException.getStatus() == 5000) {
			entity = new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (applicationException.getStatus() == 4000) {
			entity = new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
		}
		if (applicationException.getStatus() == 4008) {
			entity = new ResponseEntity<>(model, HttpStatus.REQUEST_TIMEOUT);
		}
	
		return entity;
	}
	
}
