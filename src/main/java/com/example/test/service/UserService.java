package com.example.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.test.entity.User;
import com.example.test.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Transactional
	public void addUser(final User user) {

		if (user.getName() == null || user.getName().trim().length() == 0) {
			return;
		}
		userRepo.save(user);
	}

	@Transactional
	public List<User> getUsers() {

		return userRepo.findAll();
	}
}
