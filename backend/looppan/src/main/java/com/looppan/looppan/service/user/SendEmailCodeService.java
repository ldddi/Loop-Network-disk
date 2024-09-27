package com.looppan.looppan.service.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SendEmailCodeService {
    public ResponseEntity<Map> sendEmailCode(String email);
}
