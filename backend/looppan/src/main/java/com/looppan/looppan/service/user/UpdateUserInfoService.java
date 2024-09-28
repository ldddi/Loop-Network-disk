package com.looppan.looppan.service.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UpdateUserInfoService {

    public ResponseEntity<Map> updateUserInfo(String nickName, MultipartFile avatar) throws IOException;
}
