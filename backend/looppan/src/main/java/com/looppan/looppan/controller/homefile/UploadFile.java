package com.looppan.looppan.controller.homefile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadFile {

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Map<String, String> uploadFile(@RequestParam("file") String file) {


        Map<String, String> mp = new HashMap<String, String>();
        mp.put("message", "success");
        return mp;
    }
}
