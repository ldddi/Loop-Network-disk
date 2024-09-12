package com.looppan.looppan.controller.homefile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CreateNewFolder {

    @RequestMapping(value = "file/newFolder", method = RequestMethod.POST)
    public Map<String, String> createNewFolder() {


        Map<String, String> mp = new HashMap<String, String>();
        mp.put("message", "success");
        return mp;
    }
}
