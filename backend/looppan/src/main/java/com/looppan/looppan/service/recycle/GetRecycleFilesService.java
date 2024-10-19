package com.looppan.looppan.service.recycle;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GetRecycleFilesService {
    ResponseEntity<Map> getRecycleFiles();
}
