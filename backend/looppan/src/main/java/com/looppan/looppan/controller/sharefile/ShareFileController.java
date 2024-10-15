package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ShareFileController {

    @Autowired
    ShareFileService shareFileService;

    @RequestMapping(value = "/file/shareFile", method = RequestMethod.POST)
    public ResponseEntity<Map> shareFile(@RequestBody Map<String, String> mp) {
        String fileId = mp.get("fileId");
        String time = mp.get("time");

        return shareFileService.shareFile(fileId, time);
    }
}
