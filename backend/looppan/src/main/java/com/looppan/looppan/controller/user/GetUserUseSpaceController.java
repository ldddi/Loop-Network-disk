package com.looppan.looppan.controller.user;

import com.looppan.looppan.service.user.GetUserUseSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetUserUseSpaceController {

    @Autowired
    GetUserUseSpaceService getUserUseSpaceService;

    @GetMapping("/user/getUseSpace")
    public ResponseEntity<Map> getUserUseSpace() {
        return getUserUseSpaceService.getUserUseSpace();
    }
}
