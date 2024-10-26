package com.looppan.looppan.controller.sharefile;


import com.looppan.looppan.service.sharefile.GetSharedFilesListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetSharedFilesListController {

    @Autowired
    GetSharedFilesListService getSharedFilesListService;

    @RequestMapping(value = "/file/getSharedFilesList", method = RequestMethod.POST)
    public ResponseEntity<Map> getSharedFilesList(@RequestBody(required = false) Map<String, String> mp) {
        String shareId = mp.get("shareId");
        return getSharedFilesListService.getSharedFilesList(shareId);
    }
}
