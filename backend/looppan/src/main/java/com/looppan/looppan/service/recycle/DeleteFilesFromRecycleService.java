package com.looppan.looppan.service.recycle;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DeleteFilesFromRecycleService {
    ResponseEntity<Map> deleteFilesFromRecycleService(List<String> filesId);
}
