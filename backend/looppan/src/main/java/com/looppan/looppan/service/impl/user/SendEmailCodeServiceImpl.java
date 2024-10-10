package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.utils.RandomUtils;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.service.user.SendEmailCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendEmailCodeServiceImpl implements SendEmailCodeService {
    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public ResponseEntity<Map> sendEmailCode(String email) {
        String code = null;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            code = RandomUtils.generateRandomString(StaticKey.EMAIL_CHECK_CODE_LENGTH.toIntegerValue());
            message.setFrom(from);
            message.setTo(email);
            message.setSubject("Loop邮箱验证码");
            message.setText("您的邮箱验证码是: " + code);
            mailSender.send(message);
        } catch (Exception e) {
            throw new MyException("邮箱发送失败！");
        }

        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(StaticKey.EMAIL_CHECK_CODE_VALID_TIME.toIntegerValue()));

        Map<String, String> mp = new HashMap<String, String>();
        mp.put("message", "邮箱验证码发送成功!");

        return ResponseEntity
                            .ok()
                            .body(mp);
    }
}
