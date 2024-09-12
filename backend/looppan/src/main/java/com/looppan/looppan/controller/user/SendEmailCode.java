package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendEmailCode {

    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/sendEmailCode", method = RequestMethod.POST)
    public String sendEmailCode(HttpSession session, @RequestParam(required = true) String email, @RequestParam(required = true) Integer type) throws MessagingException {
        try {
            emailService.sendEmailCode(email, 0);   // 0 注册 1 忘记密码
            return StaticKey.MESSAGE_SUCCESS;
        } finally {
            session.removeAttribute(StaticKey.EMAIL_CHECK_CODE_KEY);
        }
    }
}
