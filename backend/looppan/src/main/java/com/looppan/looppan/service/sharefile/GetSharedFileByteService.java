package com.looppan.looppan.service.sharefile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

public interface GetSharedFileByteService {

    ResponseEntity<FileSystemResource> getSharedFile(String fileId);
}
