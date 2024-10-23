package com.looppan.looppan.service.user;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface GetUserAvatarByteService {
    ResponseEntity<FileSystemResource> getUserAvatarByte(String avatarPath) throws IOException;
}
