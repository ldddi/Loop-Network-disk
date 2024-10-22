package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.ReturnFileByteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ReturnFileByteController {

    @Autowired
    ReturnFileByteService returnFileByteService;

    @PostMapping("/file/returnFileByte")
    public ResponseEntity<FileSystemResource> returnFileByte(@RequestBody Map<String, String> mp) {
        String fileId = mp.get("fileId");

        return returnFileByteService.returnFileByte(fileId);
    }
}

