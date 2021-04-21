package com.example.user.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.user.entity.User;
import com.example.user.exception.ApplicationException;
import com.example.user.service.UserService;
import com.example.user.view.ResponseModel;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(value = "/registration")
	public ResponseEntity<ResponseModel> registration(@RequestBody final User user) throws ApplicationException {
		ResponseModel responseModel = new ResponseModel();
		userService.registration(user);
		responseModel.setStatus(2001);
		responseModel.setMessage("Registered Successfully");
		return new ResponseEntity<>(responseModel, HttpStatus.CREATED);
	}

	@PostMapping(value = "/login")
	public ResponseModel login(@RequestBody final User user) throws ApplicationException {
		ResponseModel responseModel = new ResponseModel();
		userService.login(user);
		responseModel.setStatus(2000);
		responseModel.setMessage("Successfully logged in");
		return responseModel;
	}

	@GetMapping(value = "/users")
	public ResponseModel getUsers() {
		ResponseModel responseModel = new ResponseModel();
		responseModel.setPayload(userService.getUsers());
		responseModel.setStatus(2000);
		responseModel.setMessage("Users List Fetched Successfully");
		return responseModel;
	}
}
