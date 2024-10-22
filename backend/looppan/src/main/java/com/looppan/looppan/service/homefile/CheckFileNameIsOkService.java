package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CheckFileNameIsOkService {
    ResponseEntity<Map> checkFileName(String filePId, List<String> fileNameList);
}
