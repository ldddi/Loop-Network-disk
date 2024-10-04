package com.looppan.looppan.controller.homefile;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadFile {

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Map<String, String> uploadFile(@RequestBody Map<String, String> createfile) {


        Map<String, String> mp = new HashMap<String, String>();
        mp.put("message", "success");
        return mp;
    }
}
