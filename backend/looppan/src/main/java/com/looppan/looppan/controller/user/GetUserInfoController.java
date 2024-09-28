package com.looppan.looppan.controller.user;

import com.looppan.looppan.service.user.GetUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetUserInfoController {

    @Autowired
    GetUserInfoService getUserInfoService;

    @PostMapping("/getUserInfo")
    public ResponseEntity<Map> GetUserInfo() {
        return getUserInfoService.getUserInfo();
    }
}
