package com.example.test.configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import com.example.test.constants.Constants;
import com.example.test.service.RedisSubscriber;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableCaching
@Slf4j
public class RedisConfiguration {

	@Autowired
	private RedisSubscriber redisSubscriber;

	private String redisPrefix = "service-user-";

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		Config redisConfig = new Config();
		String hostNamesWithSSL = Constants.REDIS_SCHEME + "localhost" + ":" + "6379";
		redisConfig.useSingleServer().setAddress(hostNamesWithSSL);
//		redisConfig.useSingleServer().setAddress(hostNamesWithSSL).setPassword("redis"); with password
		return new RedissonConnectionFactory(redisConfig);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		final GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper());

		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setDefaultSerializer(serializer);
		return redisTemplate;
	}

	@Bean(name = "statusRedisContainer")
	@DependsOn(value = { "redisTemplate" })
	RedisMessageListenerContainer redisContainer(RedisConnectionFactory statusRedisConnectionFactory,
			RedisTemplate<String, String> statusRedisTemplate) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(statusRedisConnectionFactory);

		final ChannelTopic configChannel = new ChannelTopic("user.topic");
		final List<ChannelTopic> redisChannels = Arrays.asList(configChannel);
		container.setRecoveryInterval(10000);
		container.addMessageListener(messageListener(), redisChannels);
		log.info("adding redis subscriber for topic - {}", redisChannels);
		return container;

	}

	@Bean(name = "statusMessageListener")
	MessageListenerAdapter messageListener() {
		return new MessageListenerAdapter(redisSubscriber);
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		Duration expiration = Duration.ofSeconds(60); // 1 minute
		log.info("configuring redis cache manger with ttl - {} seconds", expiration.getSeconds());
		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(RedisCacheConfiguration
				.defaultCacheConfig().prefixCacheNameWith(redisPrefix).entryTtl(expiration).disableCachingNullValues())
				.build();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		mapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
		mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
		return mapper;
	}
}
