package com.looppan.looppan.service.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface GetPicCheckCodeService {
    public ResponseEntity<byte[]> getPicCheckCode(HttpSession session) throws IOException;
}
