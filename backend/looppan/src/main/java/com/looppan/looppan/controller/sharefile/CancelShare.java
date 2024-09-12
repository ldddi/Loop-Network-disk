package com.looppan.looppan.controller.sharefile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CancelShare {

    @RequestMapping(value = "/share/cancelFile", method = RequestMethod.POST)
    public Map<String, String> cancelFile() {
        Map<String, String> mp = new HashMap<>();
        mp.put("message", "success");
        return mp;
    }
}
