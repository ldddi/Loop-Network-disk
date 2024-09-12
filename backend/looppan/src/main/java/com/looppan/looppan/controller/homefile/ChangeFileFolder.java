package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ChangeFileFolder {

    @Autowired
    FileInfoMapper fileInfoMapper;

    @RequestMapping(value = "/file/changeFileFolder", method = RequestMethod.POST)
    public Map<String, String> changeFileFolder() {

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "success");
        return mp;
    }
}
