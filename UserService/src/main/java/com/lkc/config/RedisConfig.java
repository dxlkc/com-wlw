package com.lkc.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.lkc.Utils.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    /**
     * json实现value的序列化
     *
     * @param objectMapper
     * @return
     */
    @Bean
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer(ObjectMapper objectMapper) {
        System.out.println("----Bean: Jackson2JsonRedisSerializer----");
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    /*** 实例化 RedisTemplate 对象
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                                     Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        System.out.println("----Bean: functionDomainRedisTemplate----");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //默认使用json序列化
        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);

        //key value都设置为string序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        return redisTemplate;
    }

    /**
     * 注入封装RedisTemplate
     */
    @Bean(name = "redisUtil")
    public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate) {
        System.out.println("----Bean: redisUtil----");
        RedisUtil redisUtil = RedisUtil.getInstance();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }

}
