package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.CheckFileNameIsOkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckFileNameIsOkServiceImpl implements CheckFileNameIsOkService {

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> checkFileName(String filePId, List<String> fileNameList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        List<FileInfo> fileInfoList = null;

        try {
            fileInfoList = fileInfoMapper.selectByFilePidAndUserIdAndLimitType(filePId, Integer.valueOf(userId), FileStaticKey.FOLDER_TYPE_FILE.toIntegerValue());
        } catch (Exception e) {
            throw new MyException("获取文件列表失败");
        }

        for (FileInfo fileInfo : fileInfoList) {
            String existingFileName = fileInfo.getFileName();
            if (fileNameList.contains(existingFileName)) {
                throw new MyException("包含已存在的文件名, 文件上传失败");
            }
        }

        Map<String, String> mp = new HashMap<>();
        return ResponseEntity.ok().body(mp);
    }
}
