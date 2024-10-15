package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.ShareCheckCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ShareCheckCodeController {

    @Autowired
    ShareCheckCodeService shareCheckCodeService;

    @RequestMapping(value = "/shareCheckCode/{fileId}/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Map> shareCheckCode(@PathVariable String fileId, @PathVariable String userId, @RequestParam(value = "code") String code) {


        return shareCheckCodeService.shareCheckCode(fileId, userId, code);
    }
}
