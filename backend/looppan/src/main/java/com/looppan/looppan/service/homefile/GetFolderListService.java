package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GetFolderListService {
    public ResponseEntity<Map> getFolderList(String filePId);
}
