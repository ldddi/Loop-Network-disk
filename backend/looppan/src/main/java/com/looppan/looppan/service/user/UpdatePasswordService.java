package com.looppan.looppan.service.user;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UpdatePasswordService {
    public ResponseEntity<Map> updatePassword(String email, String password, String confirmPassword, String emailCheckCode);
}
