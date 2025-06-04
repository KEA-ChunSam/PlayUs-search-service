package com.playus.searchservice.global.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Profile({"local", "test"})
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean(name = "trendingKeywordRedisTemplate")
    public RedisTemplate<String, String> trendingKeywordRedisTemplate() {

        RedisTemplate<String, String> trendingKeywordRedisTemplate = new RedisTemplate<>();
        trendingKeywordRedisTemplate.setConnectionFactory(redisConnectionFactory());

        trendingKeywordRedisTemplate.setKeySerializer(new StringRedisSerializer());
        trendingKeywordRedisTemplate.setValueSerializer(new StringRedisSerializer());

        return trendingKeywordRedisTemplate;
    }
}
