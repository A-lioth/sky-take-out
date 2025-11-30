package com.sky.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        // * 配置 RedisTemplate
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // * 配置 key 的序列化方式为 String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // * 配置 Hash 的 key 的序列化方式为 String
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // * 配置 value 的序列化方式为 Json
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }
}
