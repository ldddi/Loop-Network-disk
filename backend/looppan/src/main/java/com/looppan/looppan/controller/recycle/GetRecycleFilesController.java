package com.looppan.looppan.controller.recycle;

import com.looppan.looppan.service.recycle.GetRecycleFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetRecycleFilesController {

    @Autowired
    GetRecycleFilesService getRecycleFilesService;

    @RequestMapping(value = "/getRecycleFiles", method = RequestMethod.GET)
    public ResponseEntity<Map> getRecycleFiles() {

        return getRecycleFilesService.getRecycleFiles();
    }
}
