package com.looppan.looppan.service.impl.sharefile;

import com.alibaba.fastjson2.JSONObject;
import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.sharefile.ShareFileService;
import com.looppan.looppan.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ShareFileServiceImpl implements ShareFileService {

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<Map> shareFile(String fileId, String time) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();
        String nickName = user.getNickName();
        String avatar = user.getAvatar();
        FileInfo fileInfo = null;
        try {
            fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, Integer.valueOf(userId));
        } catch (Exception e) {
            throw new MyException("获取文件信息失败");
        }
        String shareId = fileId + userId;

        if (fileShareMapper.selectById(shareId) != null) {
            throw new MyException("文件已分享");
        }

        String code = RandomUtils.generateRandomString(FileStaticKey.LENGTH_SHARE_CODE.toIntegerValue());
        LocalDateTime now = LocalDateTime.now();
        if (Objects.equals(time, "perpetual")) {
            time = "0";
        }

        FileShared fileShared = new FileShared();
        fileShared.setShareId(shareId);
        fileShared.setFileId(fileId);
        fileShared.setUserId(Integer.valueOf(userId));
        fileShared.setUserNickName(nickName);
        fileShared.setUserAvatar(avatar);
        fileShared.setFileName(fileInfo.getFileName());
        fileShared.setFilePath(fileInfo.getFilePath());
        fileShared.setFileSize(fileInfo.getFileSize());
        fileShared.setFileCategory(fileInfo.getFileCategory());
        fileShared.setShareTime(now);
        fileShared.setFailTime(now.plusDays(Long.parseLong(time)));
        fileShared.setExtractionCode(code);
        String url = "http:localhost:1030/shareFilesInfo/" + fileId + "/" + userId;
        fileShared.setFileUrl(url);
        fileShareMapper.insert(fileShared);

        Map<String, Object> mp = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("code", code);
        mp.put("message", "分享成功");
        mp.put("data", jsonObject);
        return ResponseEntity.ok().body(mp);
    }
}
