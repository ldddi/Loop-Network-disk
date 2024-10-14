package com.looppan.looppan.service.homefile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

public interface DownloadService {
    public ResponseEntity<FileSystemResource> download(String fileId);
}
