package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public interface CreateFileService {
    public ResponseEntity<Map> createFile(String filePId, String fileName);
}
