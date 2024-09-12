package com.looppan.looppan.controller.config;

import com.looppan.looppan.controller.user.utils.StaticKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RedisInitService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        try {
            System.out.println("Initializing Redis values...");
            redisTemplate.opsForValue().set(StaticKey.REDIS_EMAIL_TITLE_KEY, StaticKey.REDIS_EMAIL_TITLE);
            redisTemplate.opsForValue().set(StaticKey.REDIS_EMAIL_CONTENT_KEY, StaticKey.REDIS_EMAIL_CONTENT);
            System.out.println("Redis initialization completed.");
        } catch (Exception e) {
            System.out.println("Error during Redis initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
