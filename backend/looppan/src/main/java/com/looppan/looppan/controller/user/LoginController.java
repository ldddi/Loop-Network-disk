package com.looppan.looppan.controller.user;

import com.looppan.looppan.service.user.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> userLogin(@RequestBody Map<String, String> requestMp, HttpSession session)
    {

        String email = requestMp.get("email");
        String password = requestMp.get("password");
        String picCheckCode = requestMp.get("picCheckCode");

        return loginService.login(email, password, picCheckCode, session);
    }

}
