package com.looppan.looppan.controller.homefile;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GetFileInfo {

    @RequestMapping(value = "/file/getFile/{fileId}", method = RequestMethod.GET)
    public Map<String, String> getFileInfo(
            @PathVariable("fileId") String fileId)
    {


        Map<String, String> mp = new HashMap<String, String>();
        mp.put("message", "success");
        return mp;
    }
}
