package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    UserMapper userMapper;

    @Override
    public ResponseEntity<Map> register(
            String email, String password, String confirmPassword,
            String emailCheckCode, String picCheckCode, HttpSession session)
    {
        if (!password.equals(confirmPassword)) {
            throw new MyException("两次密码不一致");
        }

        String picCode = redisTemplate.opsForValue().get(session.getId());
        if (picCode != null) {
            redisTemplate.delete(session.getId());
        }
        String emailCOde = redisTemplate.opsForValue().get(email);
        if (emailCOde != null) {
            redisTemplate.delete(email);
        }

        if (picCode == null || !picCode.equalsIgnoreCase(picCheckCode)) {
            throw new MyException("图片验证码错误");
        }

        if (emailCOde == null || !emailCOde.equals(emailCheckCode)) {
            throw new MyException("邮箱验证码错误");
        }

        if (userMapper.selectByEmail(email) != null) {
            throw new MyException("邮箱已存在");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setJoinTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        user.setTotalSpace(BigInteger.valueOf(StaticKey.ALL_SPACE.toIntegerValue()));
        user.setAvatar(StaticKey.AVATAR_URL.toStringValue());
        user.setNickName(email);
        userMapper.insert(user);

        Map<String, String> mp = new HashMap<String, String>();
        mp.put("message", "注册成功!~");
        return ResponseEntity
                .ok()
                .body(mp);
    }
}
