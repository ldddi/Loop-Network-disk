package com.looppan.looppan.controller.user;

import com.alibaba.fastjson2.JSONObject;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GetUserSpace {

    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/getUserSpace", method = RequestMethod.GET)
    public Map<String, String> getUserSpace(HttpSession session) {
        Map<String, String> mp = new HashMap<>();
        int userId = (int) session.getAttribute(StaticKey.USER_ID_KEY);
        User user = userMapper.selectById(userId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("useSpace", user.getUseSpace());
        jsonObject.put("totalSpace", user.getTotalSpace());
        mp.put("message", "success");
        mp.put("data", jsonObject.toJSONString());
        return mp;
    }
}
