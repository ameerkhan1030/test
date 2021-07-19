package com.example.test.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.example.test.entity.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class AspectUserService {

	@Before("execution(* com.example.test.service.UserService.addUser(..))")
	public void logBeforeAddUser(JoinPoint joinPoint) {

		User user = (User) joinPoint.getArgs()[0];
		log.info("Aspect Log: Adding User {}", user.getName());
	}

	@After("execution(* com.example.test.service.UserService.addUser(..))")
	public void logAfterAddUser(JoinPoint joinPoint) {

		User user = (User) joinPoint.getArgs()[0];
		log.info("Aspect Log: User Created Successfully {}", user.getName());
	}

}
