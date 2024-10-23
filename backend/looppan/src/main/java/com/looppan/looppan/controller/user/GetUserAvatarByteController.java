package com.looppan.looppan.controller.user;

import com.looppan.looppan.service.user.GetUserAvatarByteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GetUserAvatarByteController {

    @Autowired
    GetUserAvatarByteService getUserAvatarByteService;

    @GetMapping(value = "/user/getAvatarByte")
    public ResponseEntity<FileSystemResource> getUserAvatarByte(@RequestParam String avatar) throws IOException {
        return getUserAvatarByteService.getUserAvatarByte(avatar);
    }
}
