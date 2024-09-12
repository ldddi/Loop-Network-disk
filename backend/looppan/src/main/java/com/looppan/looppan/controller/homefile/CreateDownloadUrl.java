package com.looppan.looppan.controller.homefile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CreateDownloadUrl {
    @RequestMapping(value = "/file/createDownloadUrl", method = RequestMethod.GET)
    public Map<String, String> createDownloadUrl() {

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "success");
        return mp;
    }
}
