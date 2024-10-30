package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.ForgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ForgetPasswordServiceImpl implements ForgetPasswordService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public ResponseEntity<Map> forgetPassword(String email, String password, String confirmPassword, String checkCode) {

        if (!password.equals(confirmPassword)) {
            throw new MyException("两次密码不一致");
        }

        User user = userMapper.selectByEmail(email);
        if (user == null) {
            throw new MyException("邮箱不存在");
        }

        String code = redisTemplate.opsForValue().get(email);
        if (code == null || !code.equals(checkCode)) {
            if (code != null) {
                redisTemplate.delete(email);
            }
            throw new MyException("邮箱验证码不正确");
        }
        Map<String, String> mp = new HashMap<>();
        mp.put("message", "修改密码成功");
        return ResponseEntity.ok().body(mp);
    }
}
