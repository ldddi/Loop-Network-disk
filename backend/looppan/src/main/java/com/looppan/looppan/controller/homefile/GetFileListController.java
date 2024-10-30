package com.looppan.looppan.controller.homefile;


import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.service.homefile.GetFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GetFileListController {

    @Autowired
    GetFileListService getFileListService;

    @RequestMapping(value = "/file/getFileList", method = RequestMethod.GET)
    public ResponseEntity<Map> getFileList(
            @RequestParam("category") String category,
            @RequestParam(value = "path", required = false) String path,
            @RequestParam("page") Integer page)
    {

        return getFileListService.getFileList(Integer.valueOf(category), path, page);
    }
}
