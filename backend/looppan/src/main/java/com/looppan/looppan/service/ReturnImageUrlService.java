package com.looppan.looppan.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface ReturnImageUrlService {

    public ResponseEntity<Resource> returnImageUrl(String fileId);
}
