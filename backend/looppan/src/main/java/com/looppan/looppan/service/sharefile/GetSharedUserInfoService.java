package com.looppan.looppan.service.sharefile;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GetSharedUserInfoService {

    public ResponseEntity<Map> getSharedUserInfo(String fileId, String userId);
}
