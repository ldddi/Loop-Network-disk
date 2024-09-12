package com.looppan.looppan.controller.homefile;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@RequestMapping
public class DownloadFile {

    @RequestMapping(value = "/file/download/{code}", method = RequestMethod.GET)
    public Map<String, String> download(
            @PathVariable("code") String code
    ) {
        Map<String, String> mp = new HashMap<>();
        mp.put("message", "success");
        return mp;
    }
}
