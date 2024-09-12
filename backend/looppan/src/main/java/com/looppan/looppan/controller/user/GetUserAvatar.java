package com.looppan.looppan.controller.user;

import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GetUserAvatar {

    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/getAvatar/{userId}", method = RequestMethod.GET)
    public Map<String, String> getUserAvatar(@PathVariable int userId) {

        Map<String, String> mp = new HashMap<String, String>();
        User user = userMapper.selectById(userId);
        String url = user.getQqAvatar();

        mp.put("message" , "success");
        mp.put("data", url);
        return mp;
    }
}
