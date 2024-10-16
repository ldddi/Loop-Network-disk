package com.looppan.looppan.service.impl.sharefile;

import com.alibaba.fastjson2.JSONObject;
import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.sharefile.GetSharedUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetSharedUserInfoServiceImp implements GetSharedUserInfoService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<Map> getSharedUserInfo(String fileId, String userId) {
        String shareId = fileId + userId;
        FileShared fileShared = null;
        try {
            fileShared = fileShareMapper.selectById(shareId);
        } catch (Exception e) {
            throw new MyException("获取分享用户信息失败");
        }

        Map<String, Object> mp = new HashMap<String, Object>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userAvatar", fileShared.getUserAvatar());
        jsonObject.put("nickName", fileShared.getUserNickName());
        jsonObject.put("fileName", fileShared.getFileName());
        jsonObject.put("shareTime", fileShared.getShareTime());

        mp.put("data", jsonObject);
        return ResponseEntity.ok().body(mp);
    }
}
