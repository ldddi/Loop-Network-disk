package com.looppan.looppan.controller.user;

import com.looppan.looppan.service.user.UpdatePasswordService;
import com.looppan.looppan.service.user.UpdateUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class UpdateUserInfoController {

    @Autowired
    UpdateUserInfoService updatePasswordService;

    @PostMapping("/updateUserInfo")
    public ResponseEntity<Map> updateUserInfo(
            @RequestParam("nickName") String nickName,
            @RequestParam(value = "avatar" , required = false) MultipartFile avatar) throws IOException {
        System.out.println(nickName);
        System.out.println(avatar);
        return updatePasswordService.updateUserInfo(nickName, avatar);
    }
}
