package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UpdatePassword {
    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Map<String, String> updatePassword(
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            HttpSession session
    ) {
        Map<String, String> mp = new HashMap<>();


        return mp;
    }
}
