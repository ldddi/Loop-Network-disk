package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ShareFileService {
    public ResponseEntity<Map> shareFile(String fileId);
}
