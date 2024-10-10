package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.GetAllCategoryFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetAllCategoryFileController {

    @Autowired
    GetAllCategoryFileService getAllCategoryFileService;

    @RequestMapping(value = "/file/getAllCategoryFile", method = RequestMethod.POST)
    public ResponseEntity<Map> getAllAudioFile(@RequestBody Map<String, String> mp) {
        Integer category = Integer.valueOf(mp.get("category"));

        return getAllCategoryFileService.getAllAudioFiles(category);
    }
}
