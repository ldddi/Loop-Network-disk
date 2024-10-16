package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.GetSharedUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetSharedUserInfoController {

    @Autowired
    GetSharedUserInfoService getSharedUserInfoService;

    @RequestMapping(value = "/getSharedUserInfo", method = RequestMethod.GET)
    public ResponseEntity<Map> getSharedUserInfo(@RequestParam Map<String, String> mp) {
        String fileId = mp.get("fileId");
        String userId = mp.get("userId");

        return getSharedUserInfoService.getSharedUserInfo(fileId, userId);
    }
}
