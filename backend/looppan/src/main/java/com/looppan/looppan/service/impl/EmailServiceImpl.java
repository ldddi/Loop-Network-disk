package com.looppan.looppan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.looppan.looppan.controller.config.AppConfig;
import com.looppan.looppan.controller.config.MyException;
import com.looppan.looppan.controller.user.utils.RandomUtils;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.EmailMapper;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.Email;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

import static java.time.LocalDateTime.*;

@Service
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements EmailService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private JavaMailSender mailSender;  // 使用实例对象

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)   // 开启事务
    public void sendEmailCode(String email, Integer type) throws MessagingException {
        if (type == 0) {    // 0 注册 1 找回密码
            User user = userMapper.selectByEmail(email);
            if (user != null) {
                throw new MyException("邮箱已经存在");
            }
        }
        int fail = StaticKey.EMAIL_CODE_FAIL;
        int ok = StaticKey.EMAIL_CODE_OK;
        emailMapper.disableEmailCode(fail, email, ok);

        String code = RandomUtils.generateRandomString(StaticKey.CHECK_CODE_LENGTH);
        sendEmailCode(email, code);
        Email email2 = new Email();
        email2.setEmail(email);
        email2.setCode(code);
        email2.setStatus(StaticKey.EMAIL_CODE_OK);
        email2.setCreateTime(now());
        emailMapper.insert(email2);
    }

    private void sendEmailCode (String toemail, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();  // 使用实例对象
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 设置邮件内容
        helper.setTo(toemail);
        helper.setSubject((String) Objects.requireNonNull(redisTemplate.opsForValue().get(StaticKey.REDIS_EMAIL_TITLE_KEY)));
        helper.setText(String.format((String) Objects.requireNonNull(redisTemplate.opsForValue().get(StaticKey.REDIS_EMAIL_CONTENT_KEY)), code) );  // HTML content
        helper.setFrom(appConfig.getEmailUsername());
        helper.setSentDate(new Date());

        // 发送邮件
        mailSender.send(message);  // 使用实例对象
    }


}
