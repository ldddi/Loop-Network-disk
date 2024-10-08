package com.looppan.looppan.service.homefile;

import com.looppan.looppan.pojo.FileInfo;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface GetFileListService {
    public ResponseEntity<List<FileInfo>> getFileList(Integer category, String path);
}
