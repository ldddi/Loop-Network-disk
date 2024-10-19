package com.looppan.looppan.service.recycle;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CancelDeleteService {
    ResponseEntity<Map> cancelDelete(List<String> filesId);
}
