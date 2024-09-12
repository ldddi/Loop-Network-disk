package com.looppan.looppan.controller.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Logout {

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Map<String, String> UserLogout(HttpSession session) {
        Map<String, String> mp = new HashMap<>();

        session.invalidate();

        mp.put("message", "success");
        return mp;
    }
}
