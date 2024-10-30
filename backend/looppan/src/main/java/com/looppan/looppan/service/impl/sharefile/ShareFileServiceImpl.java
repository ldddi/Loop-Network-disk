package com.looppan.looppan.service.impl.sharefile;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.codec.FieldInfo;
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
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ShareFileServiceImpl implements ShareFileService {

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    @Transactional
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

        if(fileInfo == null) {
            throw new MyException("获取文件失败");
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

        try {
            fileInfo.setShared(true);
            fileInfoMapper.updateSharedByFileIdAndUserId(fileId, userId, true);
        } catch (Exception e) {
            throw new MyException("插入失败2");
        }

        Path filePath = Paths.get(fileInfo.getFilePath());
        if (Files.isDirectory(filePath)) {
            insertFileSharedDir(fileInfo, shareId, userId);
        }

        Map<String, Object> mp = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("code", code);
        mp.put("message", "分享成功");
        mp.put("data", jsonObject);
        return ResponseEntity.ok().body(mp);
    }

    private void insertFileSharedDir(FileInfo fileInfo, String sharePId, String userId) {
        List<FileInfo> fileInfos = fileInfoMapper.selectByFilePidAndUserId2(fileInfo.getFileId(), Integer.valueOf(userId));
        for (FileInfo f : fileInfos) {
            if (Objects.equals(f.getFolderType(), FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue())) {
                insertFileShared(f, sharePId, userId);
                insertFileSharedDir(f, sharePId, userId);
            } else {
                insertFileShared(f, sharePId, userId);
            }
        }
    }

    private void insertFileShared(FileInfo fileInfo, String sharePId, String userId) {
        FileShared fileShared = new FileShared();
        String shareId = RandomUtils.generateRandomString(FileStaticKey.LENGTH_SHARE_ID.toIntegerValue());
        fileShared.setShareId(shareId);
        fileShared.setSharePId(sharePId);
        fileShared.setFileId(fileInfo.getFileId());
        fileShared.setFileCategory(fileInfo.getFileCategory());
        fileShared.setFilePath(fileInfo.getFilePath());
        fileShared.setUserId(Integer.valueOf(userId));
        fileShared.setFileName(fileInfo.getFileName());
        fileShared.setFileSize(fileInfo.getFileSize());

        try {
            fileShareMapper.insert(fileShared);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
