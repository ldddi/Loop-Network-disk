package com.looppan.looppan.controller.recycle;

import com.looppan.looppan.service.recycle.DeleteFilesFromRecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DeleteFilesFromRecycleController {

    @Autowired
    DeleteFilesFromRecycleService deleteFilesFromRecycleService;

    @RequestMapping(value = "/deleteRecycleFiles", method = RequestMethod.POST)
    public ResponseEntity<Map> deleteFiles(@RequestBody Map<String, Object> mp) {
        List<String> filesId = (List<String>) mp.get("filesId");
        return deleteFilesFromRecycleService.deleteFilesFromRecycleService(filesId);
    }
}
