package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    String uploadDir;

    @Value("${file.avatar-path}")
    String avatarPath;

    @Override
    public ResponseEntity<Map> register(
            String email, String password, String confirmPassword,
            String emailCheckCode, String picCheckCode, HttpSession session) {
        if (!password.equals(confirmPassword)) {
            throw new MyException("两次密码不一致");
        }

        String picCode = redisTemplate.opsForValue().get(session.getId());
        if (picCode != null) {
            redisTemplate.delete(session.getId());
        }

        if (picCode == null || !picCode.equalsIgnoreCase(picCheckCode)) {
            throw new MyException("图片验证码错误");
        }

        String emailCode = redisTemplate.opsForValue().get(email);

        if (emailCode == null || !emailCode.equals(emailCheckCode)) {
            throw new MyException("邮箱验证码错误");
        }

        if (emailCode != null) {
            redisTemplate.delete(email);
        }

        if (userMapper.selectByEmail(email) != null) {
            throw new MyException("邮箱已存在");
        }

        User user = new User();
        user.setEmail(email);
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        user.setJoinTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        user.setTotalSpace(BigInteger.valueOf(StaticKey.ALL_SPACE.toIntegerValue()));
        user.setUseSpace(BigInteger.valueOf(0));
        user.setAvatar(avatarPath);
        user.setNickName(email);


        try {
            userMapper.insert(user);
        } catch (Exception e) {
            throw new MyException("用户插入失败");
        }

        Path path = Paths.get(uploadDir, user.getUserId());

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new MyException("创建用户根目录失败");
        }

        if (userMapper.selectCount() >= 10) {
            List<User> us = userMapper.selectLast();
            for (User u : us) {
                if (Objects.equals(u.getEmail(), "admin")) {
                    continue;
                }
                Path p = Paths.get(uploadDir, u.getUserId());
                try {
                    deleteDirectory(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                userMapper.deleteById(u);
                break;
            }
        }

        Map<String, String> mp = new HashMap<String, String>();
        mp.put("message", "注册成功!~");
        return ResponseEntity
                .ok()
                .body(mp);
    }
    private void deleteDirectory(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    deleteDirectory(entry); // 递归删除子目录
                }
            }
        }
        Files.delete(path); // 删除文件或空目录
    }
}
