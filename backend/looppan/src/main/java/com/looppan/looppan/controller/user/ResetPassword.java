package com.looppan.looppan.controller.user;

import com.looppan.looppan.mapper.EmailMapper;
import com.looppan.looppan.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ResetPassword {

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private UserMapper userMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
    public Map<String, String> resetPassword(@RequestParam String email,
                                             @RequestParam String newPassword,
                                             @RequestParam String confirmNewPassword,
                                             @RequestParam String picCheckCode,
                                             @RequestParam String emailCode,
                                             HttpSession session)
    {
        Map<String, String> mp = new HashMap<>();
        return mp;
    }
}
