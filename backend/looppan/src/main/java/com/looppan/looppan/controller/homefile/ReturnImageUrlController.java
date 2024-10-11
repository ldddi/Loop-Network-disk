package com.looppan.looppan.controller.homefile;


import com.looppan.looppan.service.ReturnImageUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ReturnImageUrlController {

    @Autowired
    ReturnImageUrlService returnImageUrlService;

    @PostMapping("/file/returnImageUrl")
    public ResponseEntity<String> returnImageUrl(@RequestBody Map<String, String> mp) {
        String fileId = mp.get("fileId");
        return returnImageUrlService.returnImageUrl(fileId);
    }

}
