package com.looppan.looppan.controller.homefile;

import com.looppan.looppan.service.homefile.UploadFileCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class UploadFileCancelController {

    @Autowired
    UploadFileCancelService uploadFileCancelService;

    @PostMapping(value = "/file/uploadFileCancel")
    public ResponseEntity<Map> uploadFileCancel(@RequestBody(required = false) Map<String, String> mp) throws IOException {
        String filePId = mp.get("filePId");
        String fileName = mp.get("fileName");

        return uploadFileCancelService.uploadFileCancel(filePId, fileName);
    }

}
