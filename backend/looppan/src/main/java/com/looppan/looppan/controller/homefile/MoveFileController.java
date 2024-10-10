package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.MoveFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MoveFileController {

    @Autowired
    MoveFileService moveFileService;

    @RequestMapping(value = "/file/moveFiles", method = RequestMethod.POST)
    public ResponseEntity<Map> moveFiles(@RequestBody Map<String, Object> mp) {
        List<String> filesId = (List<String>) mp.get("filesId");
        String pId = (String) mp.get("pId");
        System.out.println(pId);
        return moveFileService.moveFiles(filesId, pId);
    }
}
