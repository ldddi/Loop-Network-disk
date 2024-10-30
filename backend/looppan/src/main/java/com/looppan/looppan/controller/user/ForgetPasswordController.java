package com.looppan.looppan.controller.user;

import com.looppan.looppan.service.user.ForgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ForgetPasswordController {

    @Autowired
    ForgetPasswordService forgetPasswordService;

    @GetMapping(value = "/forgetPassword")
    public ResponseEntity<Map> forgetPassword(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String emailCheckCode
    ) {
        return forgetPasswordService.forgetPassword(email, password, confirmPassword, emailCheckCode);
    }
}
