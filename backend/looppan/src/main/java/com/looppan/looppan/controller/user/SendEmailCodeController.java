package com.looppan.looppan.controller.user;

import com.looppan.looppan.service.user.SendEmailCodeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SendEmailCodeController {

    @Autowired
    SendEmailCodeService sendEmailCodeService;

    @RequestMapping(value = "/sendEmailCode", method = RequestMethod.POST)
    public ResponseEntity<Map> sendEmailCode(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        return sendEmailCodeService.sendEmailCode(email);
    }
}
