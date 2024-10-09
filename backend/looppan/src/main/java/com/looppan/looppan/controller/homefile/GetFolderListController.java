package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.GetFolderListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GetFolderListController {

    @Autowired
    GetFolderListService getFolderListService;

    @RequestMapping(value = "/file/getFolderList", method = RequestMethod.GET)
    public ResponseEntity<Map> getFolderList(@RequestParam Map<String,String> mp) {
        String filePId = mp.get("filePId");
        return getFolderListService.getFolderList(filePId);
    }
}
