package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MoveFileService {
    public ResponseEntity<Map> moveFiles(List<String> filesId, String pId);
}
