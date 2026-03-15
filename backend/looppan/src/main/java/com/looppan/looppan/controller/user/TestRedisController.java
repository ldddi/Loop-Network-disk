package com.looppan.looppan.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestRedisController {

    private final RedisTemplate redisTemplate;

    @GetMapping("/test/redis")
    public String TestRedis() {
        redisTemplate.opsForValue().set("test", "redis");
        return (String) redisTemplate.opsForValue().get("test");
    }
}
