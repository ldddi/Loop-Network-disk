package com.looppan.looppan.service.homefile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UploadFileCancelService {

    ResponseEntity<Map> uploadFileCancel(String filePId, String fileName) throws IOException;
}
