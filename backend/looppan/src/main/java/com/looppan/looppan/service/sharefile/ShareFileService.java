package com.looppan.looppan.service.sharefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ShareFileService {
    public ResponseEntity<Map> shareFile(String fileId, String time);
}
