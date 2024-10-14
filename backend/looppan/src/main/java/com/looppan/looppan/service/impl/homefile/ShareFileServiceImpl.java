package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.service.homefile.ShareFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShareFileServiceImpl implements ShareFileService {
    @Override
    public ResponseEntity<Map> shareFile(String fileId) {
        return null;
    }
}
