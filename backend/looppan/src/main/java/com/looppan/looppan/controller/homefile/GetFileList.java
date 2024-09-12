package com.looppan.looppan.controller.homefile;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GetFileList {

    @RequestMapping(value = "/file/loadDataList", method = RequestMethod.POST)
    public Map<String, String> getFileList() {

        Map<String, String> mp = new HashMap<String, String>();
        mp.put("message", "success");
        return mp;
    }
}
