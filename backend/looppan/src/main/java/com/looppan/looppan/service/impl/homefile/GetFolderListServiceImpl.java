package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.GetFolderListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetFolderListServiceImpl implements GetFolderListService {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> getFolderList(String filePId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        List<FileInfo> fileInfos = null;
        try {
            fileInfos = fileInfoMapper.selectByFilePidAndUserIdAndLimitType(filePId, Integer.valueOf(userId), FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue());
        } catch (RuntimeException e) {
            throw new MyException("获取所有pid下的文件夹失败");
        }
        Map<String, Object> mp = new HashMap<>();

        mp.put("data", fileInfos);
        return ResponseEntity.ok().body(mp);
    }
}
