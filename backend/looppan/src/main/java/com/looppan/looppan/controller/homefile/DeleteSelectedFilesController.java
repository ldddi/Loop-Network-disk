package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.DeleteSelectedFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DeleteSelectedFilesController {

    @Autowired
    DeleteSelectedFilesService deleteSelectedFilesService;

    @RequestMapping(value = "/file/deleteSelectedFiles", method = RequestMethod.POST)
    public ResponseEntity<Map> delFile(@RequestBody Map<String, Object> mp) {

        List<String> filesId = (List<String>) mp.get("filesId");

        return deleteSelectedFilesService.deleteFiles(filesId);
    }
}
