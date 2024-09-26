package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.JudgeCheckCode;
import com.looppan.looppan.mapper.EmailMapper;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Register {

    @Autowired
    UserMapper userMapper;

    @Autowired
    EmailMapper emailMapper;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, String> userRegister(@RequestBody Map<String, String> requestMp,
                               HttpSession session
    ) throws IOException {
       Map<String, String> mp = new HashMap<>();
        return mp;
    }
}
