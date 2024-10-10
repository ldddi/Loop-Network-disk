package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface RenameFileService {
    public ResponseEntity<Map> renameFile(String fileId, String newName);
}
