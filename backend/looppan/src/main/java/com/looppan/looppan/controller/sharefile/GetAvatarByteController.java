package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.GetAvatarByteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GetAvatarByteController {

    @Autowired
    private GetAvatarByteService getAvatarByteService;

    @GetMapping(value = "/getAvatarByte")
    public ResponseEntity<FileSystemResource> getAvatarByte(@RequestParam String filePath) throws IOException {

        return getAvatarByteService.getAvatar(filePath);
    }
}
