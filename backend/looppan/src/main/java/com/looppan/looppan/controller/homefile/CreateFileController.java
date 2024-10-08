package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.CreateFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class CreateFileController {

    @Autowired
    CreateFileService createFileService;

    @RequestMapping(value = "/file/createFile", method = RequestMethod.POST)
    public ResponseEntity<Map> createNewFolder(@RequestBody Map<String, String> createfile) {
        String filePId = createfile.get("filePId");
        String fileName = createfile.get("fileName");

        return createFileService.createFile(filePId, fileName);
    }
}
