package com.looppan.looppan.controller.recycle;

import com.looppan.looppan.service.recycle.CancelDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CancelDeleteController {

    @Autowired
    CancelDeleteService cancelDeleteService;

    @PostMapping("/cancelDelete")
    public ResponseEntity<Map> cancelDelete(@RequestBody Map<String, Object> mp) {
        List<String> filesId = (List<String>) mp.get("filesId");
        return cancelDeleteService.cancelDelete(filesId);
    }
}
