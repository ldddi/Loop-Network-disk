package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.GetFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GetFileListServiceImpl implements GetFileListService {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<List<FileInfo>> getFileList(Integer category, String path) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Integer userId = Integer.valueOf(user.getUserId());
        List<FileInfo> fileInfos;
        if (category == FileStaticKey.FILE_CATEGORY_FOLDER.toIntegerValue()) {
            if (path == null) {
                fileInfos = fileInfoMapper.selectByUserId(userId, "0");
            } else {
                String FilePId = path.substring(path.lastIndexOf("/") + 1);
                fileInfos = fileInfoMapper.selectByFilePidAndUserId(FilePId, userId);
            }
        } else {
            fileInfos = fileInfoMapper.selectByUserIdAndCategory(userId, category);
        }

        return ResponseEntity.ok().body(fileInfos);
    }
}
