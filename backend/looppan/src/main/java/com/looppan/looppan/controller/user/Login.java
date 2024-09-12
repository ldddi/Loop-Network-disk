package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.JudgeCheckCode;
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
public class Login {

    @Autowired
    UserMapper userMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> userLogin(@RequestParam String email,
                                         @RequestParam String password,
                                         @RequestParam String picCheckCode, HttpSession session)
    {
        User user = userMapper.selectByEmail(email);
        Map<String, String> mp = new HashMap<>();
        if (email == null || user == null || !encoder.matches(user.getPassword(), password)) {
            mp.put("message", "密码错误");
            return mp;
        }

        if (!JudgeCheckCode.isOkPicCheckCode(picCheckCode, session)) {
            mp.put("message", "图片验证码错误");
            return mp;
        }

        mp.put("message", "ok");
        return mp;
    }

}
