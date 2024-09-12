package com.looppan.looppan.controller.sharefile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ShareFile {

    @RequestMapping(value = "/share/shareFile", method = RequestMethod.POST)
    public Map<String, String> shareFile() {

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "success");
        return mp;
    }
}
