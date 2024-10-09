package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DeleteSelectedFilesService {
    public ResponseEntity<Map> deleteFiles(List<String> filesId);

}
