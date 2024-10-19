package com.looppan.looppan.service.sharefile;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CancelSharedFileService {
    public ResponseEntity<Map> cancelSharedFile(List<String> shareIds);
}
