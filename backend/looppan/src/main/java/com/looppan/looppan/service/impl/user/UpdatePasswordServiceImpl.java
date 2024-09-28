package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.UpdatePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdatePasswordServiceImpl implements UpdatePasswordService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<Map> updatePassword(String email, String password, String confirmPassword, String emailCheckCode) {
        if (!password.equals(confirmPassword)) {
            throw new MyException("两次密码不一致");
        }

        String code = stringRedisTemplate.opsForValue().get(email);
        if (code == null || !code.equals(emailCheckCode)) {
            throw new MyException("邮箱验证码错误");
        }

        User user = userMapper.selectByEmail(email);
        String encode = bCryptPasswordEncoder.encode(password);
        user.setPassword(encode);
        userMapper.updateById(user);
        Map<String, String> mp = new HashMap<>();
        mp.put("message", "success");
        return ResponseEntity.ok().body(mp);
    }
}
