package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.JudgeCheckCode;
import com.looppan.looppan.mapper.EmailMapper;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.Email;
import com.looppan.looppan.pojo.User;
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
        Map<String, String> mp = new HashMap<String, String>();
        User user = userMapper.selectByEmail(email);
        if (userMapper.selectByEmail(email) == null) {
            mp.put("message", "邮箱不存在");
            return mp;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            mp.put("message", "两次密码不一致");
            return mp;
        }

        if (!JudgeCheckCode.isOkPicCheckCode(picCheckCode,session)) {
            mp.put("message", "图片验证码错误");
            return mp;
        }

        if (!JudgeCheckCode.isOkEmailCheckCode(emailMapper, email, emailCode)) {
            mp.put("message", "邮箱验证码错误");
            return mp;
        }

        String encode = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encode);
        System.out.println("hhh");
        userMapper.updateById(user);
        System.out.println("hihihi");
        mp.put("message", "success");
        return mp;
    }
}
