package com.example.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.test.entity.User;
import com.example.test.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Transactional
	public void addUser(final User user) {

		String name = user.getName();
		if (name == null || name.trim().length() == 0) {
			log.warn("Name shouldnt be null or empty");
			return;
		}
		
		User existingUserWithSameName = userRepo.findByName(name);
		if (existingUserWithSameName != null) {
			log.warn("Name is already existing");
			return;
		}

		userRepo.save(user);
		log.info("User Created Successfully");
	}

	@Transactional
	public List<User> getUsers() {

		return userRepo.findAll();
	}
}
