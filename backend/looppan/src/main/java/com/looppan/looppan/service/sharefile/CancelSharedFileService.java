package com.looppan.looppan.service.sharefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CancelSharedFileService {
    public ResponseEntity<Map> cancelSharedFile(String shareId);
}
