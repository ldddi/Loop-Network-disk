package com.looppan.looppan.service.sharefile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface DownSharedFileService {
    public ResponseEntity<FileSystemResource> downFile(String shareId);
}
