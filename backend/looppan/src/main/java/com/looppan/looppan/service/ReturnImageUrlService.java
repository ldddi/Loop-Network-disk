package com.looppan.looppan.service;

import org.springframework.http.ResponseEntity;

public interface ReturnImageUrlService {

    public ResponseEntity<String> returnImageUrl(String fileId);
}
