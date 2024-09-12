package com.looppan.looppan.controller.homefile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FileRename {

    @RequestMapping(value = "/file/rename", method = RequestMethod.POST)
    public Map<String, String> renameFile() {


        Map<String, String> mp = new HashMap<>();
        mp.put("message", "success");
        return mp;
    }
}
