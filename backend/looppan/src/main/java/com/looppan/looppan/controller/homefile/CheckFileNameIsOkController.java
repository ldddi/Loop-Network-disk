package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.CheckFileNameIsOkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CheckFileNameIsOkController {

    @Autowired
    CheckFileNameIsOkService checkFileNameIsOkService;

    @PostMapping(value = "/file/checkFilename")
    public ResponseEntity<Map> checkFileNameIsOk(@RequestBody Map<String, Object> mp) {
        String filePId = (String) mp.get("filePId");
        List<String> fileNameList = (List<String>) mp.get("fileNameList");

        return checkFileNameIsOkService.checkFileName(filePId, fileNameList);
    }
}
