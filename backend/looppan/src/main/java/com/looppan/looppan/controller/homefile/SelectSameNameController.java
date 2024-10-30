package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.SelectSameNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SelectSameNameController {

    @Autowired
    SelectSameNameService selectSameNameService;

    @GetMapping("/file/search")
    public ResponseEntity<Map> selectSameName(@RequestParam String filePId, @RequestParam String fileName, @RequestParam Integer type){


        return selectSameNameService.selectSameName(filePId, fileName, type);
    }
}
