package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.UpdatePasswordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UpdatePassword {
    @Autowired
    private UpdatePasswordService updatePasswordService;

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public ResponseEntity<Map> updatePassword(@RequestBody Map<String,String> requestBody) {
        String email = requestBody.get("email");
        String password = requestBody.get("password");
        String confirmPassword = requestBody.get("confirmPassword");
        String emailCheckCode = requestBody.get("emailCheckCode");


        return updatePasswordService.updatePassword(email, password, confirmPassword, emailCheckCode);
    }
}
