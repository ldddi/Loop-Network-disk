package com.looppan.looppan.service.impl.sharefile;

import com.alibaba.fastjson2.JSONObject;
import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.service.sharefile.GetSharedFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetSharedFileInfoServiceImpl implements GetSharedFileInfoService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<Map> getSharedFileInfo(String fileId, String userId, String code) {

        String shareId = fileId + userId;
        FileShared fileShared = null;
        try {
            fileShared = fileShareMapper.selectById(shareId);
        } catch (Exception e) {
            throw new MyException("获取分享文件信息失败");
        }
        String extractionCode = fileShared.getExtractionCode();
        if (!code.equals(extractionCode)) {
            throw new MyException("提取码不匹配");
        }

        Map<String, Object> mp = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shareId", fileShared.getShareId());
        jsonObject.put("fileName", fileShared.getFileName());
        jsonObject.put("shareTime", fileShared.getShareTime());
        jsonObject.put("fileSize", fileShared.getFileSize());
        jsonObject.put("fileCategory", fileShared.getFileCategory());
        jsonObject.put("filePath", fileShared.getFilePath());
        mp.put("data", jsonObject);
        return ResponseEntity.ok().body(mp);
    }
}
