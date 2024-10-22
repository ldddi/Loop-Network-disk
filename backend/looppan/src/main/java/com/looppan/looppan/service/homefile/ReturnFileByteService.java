package com.looppan.looppan.service.homefile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

public interface ReturnFileByteService {
    ResponseEntity<Object> returnFileByte(String fileId);
}
