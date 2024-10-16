package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.GetSharedFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetSharedFileInfoController {

    @Autowired
    GetSharedFileInfoService getSharedFileInfoService;

    @RequestMapping(value = "/shareFileInfo", method = RequestMethod.GET)
    public ResponseEntity<Map> shareFileInfo(String fileId, String userId, String code) {

        return getSharedFileInfoService.getSharedFileInfo(fileId, userId, code);
    }
}
