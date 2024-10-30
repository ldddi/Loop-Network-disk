package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GetFileListService {
    public ResponseEntity<Map> getFileList(Integer category, String path, Integer page);
}
