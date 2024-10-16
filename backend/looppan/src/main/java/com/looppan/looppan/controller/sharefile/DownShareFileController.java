package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.DownSharedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownShareFileController {

    @Autowired
    DownSharedFileService downSharedFileService;

    @RequestMapping(value = "/downloadSharedFile", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> downloadSharedFile(String shareId) {

        return downSharedFileService.downFile(shareId);
    }
}
