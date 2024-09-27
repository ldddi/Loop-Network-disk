package com.looppan.looppan.controller.user;

import com.looppan.looppan.service.user.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Map> userRegister(@RequestBody Map<String, String> requestMp,
                                       HttpSession session
    ) throws IOException {
        String email = requestMp.get("email");
        String password = requestMp.get("password");
        String confirmPassword = requestMp.get("confirmPassword");
        String emailCheckCode = requestMp.get("emailCheckCode");
        String picCheckCode = requestMp.get("picCheckCode");

        return registerService.register(email, password, confirmPassword, emailCheckCode, picCheckCode,session);
    }
}
