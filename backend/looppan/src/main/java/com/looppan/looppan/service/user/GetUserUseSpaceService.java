package com.looppan.looppan.service.user;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GetUserUseSpaceService {

    ResponseEntity<Map> getUserUseSpace();
}
