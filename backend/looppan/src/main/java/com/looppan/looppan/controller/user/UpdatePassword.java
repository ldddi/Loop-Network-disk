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

        if (!confirmNewPassword.equals(newPassword)) {
            mp.put("message", "两次密码不一致");
            return mp;
        }
        int userId = (int) session.getAttribute(StaticKey.USER_ID_KEY);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userMapper.selectById(userId);
        String code = encoder.encode(newPassword);
        user.setPassword(code);
        userMapper.updateById(user);
        mp.put("message", "success");
        return mp;
    }
}
