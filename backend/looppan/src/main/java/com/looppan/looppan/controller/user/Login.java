package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.JudgeCheckCode;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Login {

    @Autowired
    UserMapper userMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> userLogin(@RequestBody Map<String, String> requestMp, HttpSession session)
    {
        String email = requestMp.get("email");
        String password = requestMp.get("password");
        String picCheckCode = requestMp.get("picCheckCode");

        User user = userMapper.selectByEmail(email);
        Map<String, String> mp = new HashMap<>();
        if (email == null || user == null || !encoder.matches(password, user.getPassword())) {
            System.out.println(user.getPassword());
            System.out.println(encoder.encode(password));
            mp.put("message", "密码错误");
            return mp;
        }

        if (!JudgeCheckCode.isOkPicCheckCode(picCheckCode, session)) {
            mp.put("message", "图片验证码错误");
            return mp;
        }

        session.setAttribute(StaticKey.USER_ID_KEY, user.getUserId());

        mp.put("message", "ok");
        return mp;
    }

}
