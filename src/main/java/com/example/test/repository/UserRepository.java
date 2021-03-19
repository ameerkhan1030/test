package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.test.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
