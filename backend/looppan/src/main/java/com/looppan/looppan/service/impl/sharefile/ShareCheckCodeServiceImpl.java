package com.looppan.looppan.service.impl.sharefile;

import com.alibaba.fastjson2.JSONObject;
import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.service.sharefile.ShareCheckCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ShareCheckCodeServiceImpl implements ShareCheckCodeService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<Map> shareCheckCode(String fileId, String userId, String code) {
        String shareId = fileId + userId;
        FileShared fileShared = null;

        try {
            fileShared = fileShareMapper.selectById(shareId);
        } catch (Exception e) {
            throw new MyException("获取分享文件失败");
        }

        String fileCode = fileShared.getExtractionCode();
        if (!Objects.equals(fileCode, code)) {
            ResponseEntity.notFound();
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "获取成功");
        return ResponseEntity.ok().body(mp);
    }
}
