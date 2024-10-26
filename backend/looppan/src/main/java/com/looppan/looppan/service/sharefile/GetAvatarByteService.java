package com.looppan.looppan.service.sharefile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface GetAvatarByteService {
    ResponseEntity<FileSystemResource> getAvatar(String filePath) throws IOException;
}
