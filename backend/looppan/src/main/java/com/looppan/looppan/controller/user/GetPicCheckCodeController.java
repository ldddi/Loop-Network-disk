package com.looppan.looppan.controller.user;

import com.looppan.looppan.service.user.GetPicCheckCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
//@CrossOrigin(origins = {"http://123.57.224.25:80", "null"})
//@CrossOrigin(origins = {"http://localhost:80", "null"})
public class GetPicCheckCodeController {

    @Autowired
    GetPicCheckCodeService getPicCheckCodeService;

    @RequestMapping(value = "/getPicCheckCode")
    public ResponseEntity<byte[]> sendCheckCode(HttpSession session) throws IOException {

        return getPicCheckCodeService.getPicCheckCode(session);
    }
}
