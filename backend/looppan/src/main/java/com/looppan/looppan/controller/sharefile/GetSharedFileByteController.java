package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.GetSharedFileByteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetSharedFileByteController {

    @Autowired
    GetSharedFileByteService getSharedFileByteService;

    @GetMapping(value = "/file/returnSharedFileByte")
    public ResponseEntity<FileSystemResource> returnSharedFileByte(@RequestParam String fileId) {

        return getSharedFileByteService.getSharedFile(fileId);
    }
}
