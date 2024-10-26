package com.looppan.looppan.controller.sharefile;

import com.looppan.looppan.service.sharefile.SaveMyPanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class SaveMyPanController {

    @Autowired
    SaveMyPanService saveMyPanService;

    @RequestMapping(value = "/saveMyPan", method = RequestMethod.GET)
    public ResponseEntity<Map> saveMyPan(String shareId, String userId) throws IOException {

        return saveMyPanService.saveMyPan(shareId, userId);
    }

}
