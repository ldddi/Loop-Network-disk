package com.looppan.looppan.controller.user;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<Map> test(@RequestBody Map<String, String> requestBody) {

        System.out.println("test: " + requestBody.get("test"));

        Map<String, String> mp = new HashMap<String, String>();
        mp.put("test", "hhhhhhh");
        return ResponseEntity.ok().body(mp);
    }
}
