package com.example.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user.entity.User;
import com.example.user.exception.ApplicationException;
import com.example.user.repository.UserRepository;
import com.example.user.utils.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Transactional
	public void registration(final User newUser) throws ApplicationException {

		String email = newUser.getEmail();
		String password = newUser.getPassword();
		
		emailPasswordValidation(email, password);

		Optional<User> user = userRepo.findByEmail(email);

		if (user.isPresent()) {
			throwException(4009, "Email is already present");
		}

		userRepo.save(newUser);
		log.info("{} registered successfully");
	}

	@Transactional
	public void login(final User loginUser) throws ApplicationException {

		String email = loginUser.getEmail();
		String password = loginUser.getPassword();

		emailPasswordValidation(email, password);

		Optional<User> user = userRepo.findByEmail(email);

		if (!user.isPresent()) {
			throwException(4001, "We do not recognize your E-mail or Password. Please try again.");
		}

		if (!password.equals(user.get().getPassword())) {
			throwException(4001, "We do not recognize your E-mail or Password. Please try again.");
		}

		log.info("{} logged in successfully");
	}

	private void emailPasswordValidation(String email, String password) throws ApplicationException {

		if (!ValidationUtils.validateEmail(email)) {
			throwException(4022, "Invalid email");
		}

		if (ValidationUtils.isEmptyString(email)) {
			throwException(4022, "Email shouldn't be empty");
		}

		if (ValidationUtils.isEmptyString(password)) {
			throwException(4022, "Password shouldn't be empty");
		}
	}

	@Transactional
	public List<User> getUsers() {

		return userRepo.findAll();
	}

	private static void throwException(final long status, final String message) throws ApplicationException {

		throw new ApplicationException(status, message, "");
	}
}
