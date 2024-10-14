package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DownloadFileController {

    @Autowired
    DownloadService downloadService;

    @RequestMapping(value = "/file/download", method = RequestMethod.POST)
    public ResponseEntity<FileSystemResource> downloadFile(@RequestBody Map<String, String> mp) {
        String fileId = mp.get("fileId");

        return downloadService.download(fileId);
    }
}
