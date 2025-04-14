package com.freepath.devpath.user.config;

import com.freepath.devpath.user.command.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisUserConfig {

    @Bean
    public RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, User> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer()); // Key는 문자열
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // Value는 객체 직렬화
        return template;
    }
}