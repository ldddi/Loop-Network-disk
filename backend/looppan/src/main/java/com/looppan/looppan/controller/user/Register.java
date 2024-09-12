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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, String> userRegister(@RequestParam String email,
                               @RequestParam String nickName,
                               @RequestParam String password,
                               @RequestParam String picCheckCode,
                               @RequestParam String emailCheckCode,
                               HttpSession session
    ) throws IOException {

        Map<String, String> mp = new HashMap<String, String>();

        if (userMapper.selectByNickName(nickName) != null) {
            mp.put("message" , "昵称重复");
            return mp;
        }

        if (!JudgeCheckCode.isOkPicCheckCode(picCheckCode, session)) {
            mp.put("message", "图片验证码错误");
            return mp;
        }

        if (!JudgeCheckCode.isOkEmailCheckCode(emailMapper, email, emailCheckCode)) {
            mp.put("message", "邮箱验证码错误");
            return mp;
        }

        User user = new User();
        user.setNickName(nickName);
        user.setEmail(email);

        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
        ClassPathResource classPathResource = new ClassPathResource("static/images/user/avatar.jpg");
        File file = classPathResource.getFile();
        String absolutePath = file.getAbsolutePath();
        user.setQqAvatar(absolutePath);

        user.setJoinTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.insert(user);
        return mp;
    }
}
