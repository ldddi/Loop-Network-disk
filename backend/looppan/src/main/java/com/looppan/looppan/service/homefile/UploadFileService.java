package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UploadFileService {

    public ResponseEntity<Map> uploadFile(List<MultipartFile> files, String filePId);
}
