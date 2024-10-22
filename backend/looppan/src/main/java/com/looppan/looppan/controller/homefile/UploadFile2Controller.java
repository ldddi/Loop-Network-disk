package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.UploadFile2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class UploadFile2Controller {

    @Autowired
    private UploadFile2Service uploadFile2Service;

    @PostMapping(value = "/file/uploadFile2")
    public ResponseEntity<Map> uploadFile2(@RequestParam("filePId") String filePId,
                                           @RequestParam("chunk") MultipartFile fileChunk,
                                           @RequestParam("fileName") String fileName,
                                           @RequestParam("fileSize") String fileSize,
                                           @RequestParam("chunkIndex") Integer index,
                                           @RequestParam("totalChunks") Integer totalChunks) throws IOException {

        return uploadFile2Service.upload(filePId, fileChunk, fileName,fileSize, index, totalChunks);
    }
}
