package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GetAllCategoryFileService {
    public ResponseEntity<Map> getAllAudioFiles(Integer category);
}
