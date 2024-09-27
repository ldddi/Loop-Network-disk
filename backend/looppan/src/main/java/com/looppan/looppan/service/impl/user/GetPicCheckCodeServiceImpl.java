package com.looppan.looppan.service.impl.user;

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

        String sessionId = session.getId();
        redisTemplate.opsForValue().set(sessionId, captcha, Duration.ofMinutes(5));

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }
}
