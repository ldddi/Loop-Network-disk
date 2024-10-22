package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UploadFile2Service {
    ResponseEntity<Map> upload(String filePId, MultipartFile fileChunk, String fileName,String fileSize, Integer index, Integer totalChunks) throws IOException;
}
