package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.GetSharedFileUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GetSharedFileUrlController {

    @Autowired
    GetSharedFileUrlService getSharedFileUrlService;

    @RequestMapping(value = "/file/getSharedFileUrl", method = RequestMethod.POST)
    public ResponseEntity<Map> getSharedFileUrl(@RequestBody Map<String, String> map) {
        String shareId = map.get("shareId");

        return getSharedFileUrlService.getSharedFileUrl(shareId);
    }
}
