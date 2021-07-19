package com.example.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.test.entity.User;

@Service
public class RedisPublisher {

	@Autowired
	private RedisTemplate<String, Object> statusRedisTemplate;

	public void broadcastConfigStatus(User user) {

		statusRedisTemplate.convertAndSend("user.topic", user);

	}


}
