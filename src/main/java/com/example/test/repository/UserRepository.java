package com.example.test.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import com.example.test.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@QueryHints(value = { @QueryHint(name = "name", value = "value") }, forCounting = false)
	public String findByName(final String name);
}
