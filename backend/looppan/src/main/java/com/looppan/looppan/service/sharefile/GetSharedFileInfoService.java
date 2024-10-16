package com.looppan.looppan.service.sharefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GetSharedFileInfoService {
    public ResponseEntity<Map> getSharedFileInfo(String fileId, String userId, String code);
}
