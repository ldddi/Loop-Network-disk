package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.StaticKey;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SendEmailCode {

    @RequestMapping(value = "/sendEmailCode", method = RequestMethod.POST)
    public String sendEmailCode(HttpSession session,
                                @RequestBody Map<String, String> requestBody,
                                @RequestParam(required = true) Integer type
    ) {
        return "hh";
    }
}
