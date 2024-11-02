package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.controller.user.utils.Captcha;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.service.user.GetPicCheckCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
public class GetPicCheckCodeServiceImpl implements GetPicCheckCodeService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public ResponseEntity<byte[]> getPicCheckCode(HttpSession session) throws IOException {
        String captcha = Captcha.generateRandomCaptcha(StaticKey.PIC_CHECK_CODE_LENGTH.toIntegerValue());
        BufferedImage image = Captcha.createCaptchaImage(captcha);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        byte[] bytes = outputStream.toByteArray();

        String sessionId = null;
        try {
            sessionId = session.getId();
        } catch (Exception e) {
            throw new MyException("获取session ID 失败");
        }

        try {
            redisTemplate.opsForValue().set(sessionId, captcha, Duration.ofMinutes(5));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("保存验证码失败");
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }
}
