package com.playus.searchservice.global.data;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@Profile({"dev", "prod"})  // dev, prod 환경에서만 사용
public class RedisStandaloneConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.timeout:5s}")
    private Duration timeout;

    // 연결 풀 설정도 spring.data.redis 경로로 통일
    @Value("${spring.data.redis.lettuce.pool.max-active:10}")
    private int maxActive;

    @Value("${spring.data.redis.lettuce.pool.max-idle:10}")
    private int maxIdle;

    @Value("${spring.data.redis.lettuce.pool.min-idle:2}")
    private int minIdle;

    @Value("${spring.data.redis.lettuce.pool.max-wait:3s}")
    private Duration maxWait;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 쿠버네티스 환경 단독 Redis 서버 설정
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);

        // 연결 풀 설정
        GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWait(maxWait);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));

        // 쿠버네티스 네트워크 최적화
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofSeconds(10))
                .keepAlive(true)
                .tcpNoDelay(true)
                .build();

        ClientOptions clientOptions = ClientOptions.builder()
                .socketOptions(socketOptions)
                .autoReconnect(true)
                .build();

        // Lettuce 풀링 설정
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .clientOptions(clientOptions)
                .commandTimeout(timeout)
                .build();

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean(name = "trendingKeywordRedisTemplate")
    public RedisTemplate<String, String> trendingKeywordRedisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setDefaultSerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}
