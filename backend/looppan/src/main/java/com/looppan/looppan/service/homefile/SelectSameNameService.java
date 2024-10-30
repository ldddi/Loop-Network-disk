package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface SelectSameNameService {
    ResponseEntity<Map> selectSameName(String filePId, String fileName, Integer type);
}
