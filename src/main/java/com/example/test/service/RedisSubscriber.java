package com.example.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.test.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisSubscriber implements MessageListener {
	
	
	@Autowired
	@Lazy
	private RedisTemplate<String, Object> statusRedisTemplate;
	
	@Autowired
	private ObjectMapper statusObjectMapper;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		final String json = new String(message.getBody());
        try {
			User user = statusObjectMapper.readValue(json, User.class);
			log.info("Redis Log : User Created Successfully {}", user.getName());

        }catch(final Exception e){
        	e.printStackTrace();
        }
		
	}
	

}
