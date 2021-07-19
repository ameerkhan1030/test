package com.example.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TestApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(TestApplication.class, args);

		System.out.println(context.getBean("userService"));

	}

}
