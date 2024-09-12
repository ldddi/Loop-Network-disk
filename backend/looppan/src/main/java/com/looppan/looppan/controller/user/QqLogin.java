package com.looppan.looppan.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class QqLogin {

    @RequestMapping(value = "/qqLogin", method = RequestMethod.POST)
    public Map<String, String> qqLogin() {
        Map<String, String> mp = new HashMap<String, String>();

        mp.put("message", "success");
        return mp;
    }
}
