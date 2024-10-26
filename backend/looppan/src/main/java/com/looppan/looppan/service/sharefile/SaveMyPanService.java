package com.looppan.looppan.service.sharefile;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public interface SaveMyPanService {
    public ResponseEntity<Map> saveMyPan(String shareId, String userId) throws IOException;
}
