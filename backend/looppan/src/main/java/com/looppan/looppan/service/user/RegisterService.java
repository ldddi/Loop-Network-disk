package com.looppan.looppan.service.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public interface RegisterService {
    public ResponseEntity<Map> register(String email, String password, String confirmPassword, String emailCheckCode, String picCheckCode, HttpSession session) ;
}
