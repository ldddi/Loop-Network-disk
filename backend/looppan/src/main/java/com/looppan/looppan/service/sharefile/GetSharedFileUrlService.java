package com.looppan.looppan.service.sharefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GetSharedFileUrlService {
    public ResponseEntity<Map> getSharedFileUrl(String shareId);
}
