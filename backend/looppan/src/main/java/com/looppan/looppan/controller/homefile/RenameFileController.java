package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.RenameFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RenameFileController {

    @Autowired
    RenameFileService renameFileService;

    @RequestMapping(value = "/file/renameFile")
    public ResponseEntity<Map> renameFile(@RequestBody Map<String, String> mp) {
        String fileId = mp.get("fileId");
        String newName = mp.get("newName");
        return renameFileService.renameFile(fileId, newName);
    }
}
