package com.looppan.looppan.service.sharefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GetSharedFilesListService {
    public ResponseEntity<Map> getSharedFilesList(String shareId);
}
