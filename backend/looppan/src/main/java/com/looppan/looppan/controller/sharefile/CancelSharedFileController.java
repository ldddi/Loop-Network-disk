package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.CancelSharedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CancelSharedFileController {

    @Autowired
    CancelSharedFileService cancelSharedFileService;

    @RequestMapping(value = "/file/cancelSharedFile", method = RequestMethod.POST)
    public ResponseEntity<Map> cancelSharedFile(@RequestBody Map<String, String> mp) {
        String shareId = mp.get("shareId");

        return cancelSharedFileService.cancelSharedFile(shareId);
    }
}
