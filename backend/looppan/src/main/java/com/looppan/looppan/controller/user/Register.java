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
        String email = requestMp.get("email");
        String password = requestMp.get("password");
        String nickName = requestMp.get("nickName");
        String picCheckCode = requestMp.get("picCheckCode");
        String emailCheckCode = requestMp.get("emailCheckCode");

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

        String encodePassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encodePassword);
        ClassPathResource classPathResource = new ClassPathResource("static/images/user/avatar.jpg");
        File file = classPathResource.getFile();
        String absolutePath = file.getAbsolutePath();
        user.setQqAvatar(absolutePath);

        user.setJoinTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.insert(user);
        mp.put("message", "success");
        return mp;
    }
}
