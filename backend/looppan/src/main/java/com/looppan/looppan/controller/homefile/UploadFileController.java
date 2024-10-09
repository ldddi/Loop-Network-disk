package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UploadFileController {

    @Autowired
    UploadFileService uploadFileService;

    @RequestMapping(value = "/file/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<Map> uploadFile(
            @RequestParam(value = "file[]", required = false) List<MultipartFile> files,
            @RequestParam(value = "filePId" , required = false) String filePId)
    {
        if (files == null) {
            Map<String, String> mp = new HashMap<>();
            mp.put("message", "取消成功");
            return ResponseEntity.ok().body(mp);
        }
        System.out.println(files);
        System.out.println(filePId);
        return uploadFileService.uploadFile(files, filePId);
    }
}
