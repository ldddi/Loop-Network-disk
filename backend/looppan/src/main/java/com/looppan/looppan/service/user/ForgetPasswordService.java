package com.looppan.looppan.service.user;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ForgetPasswordService {
    ResponseEntity<Map> forgetPassword(String email, String password, String confirmPassword, String checkCode);
}
