package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SendEmailCode {

    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/sendEmailCode", method = RequestMethod.POST)
    public String sendEmailCode(HttpSession session,
                                @RequestBody Map<String, String> requestBody,
                                @RequestParam(required = true) Integer type
    ) throws MessagingException {
        String email = requestBody.get("email");
        try {
            emailService.sendEmailCode(email, type);   // 0 注册 1 忘记密码
            return StaticKey.MESSAGE_SUCCESS;
        } finally {
            session.removeAttribute(StaticKey.EMAIL_CHECK_CODE_KEY);
        }
    }
}
