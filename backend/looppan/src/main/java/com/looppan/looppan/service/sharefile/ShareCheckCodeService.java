package com.looppan.looppan.service.sharefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ShareCheckCodeService {
    public ResponseEntity<Map> shareCheckCode(String fileId, String userId, String code);
}
