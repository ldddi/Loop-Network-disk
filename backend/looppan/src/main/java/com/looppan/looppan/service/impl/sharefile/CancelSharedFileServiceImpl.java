package com.looppan.looppan.service.impl.sharefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.sharefile.CancelSharedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CancelSharedFileServiceImpl implements CancelSharedFileService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    @Transactional
    public ResponseEntity<Map> cancelSharedFile(List<String> shareIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();


        try {
            for (String shareId : shareIds) {
                String fileId = shareId.substring(0, shareId.length() - userId.length());
                FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, Integer.valueOf(userId));
                if (Objects.equals(fileInfo.getFolderType(), FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue())) {
                    List<FileShared> fileShareds = fileShareMapper.selectShareListBySharePIdAndUserId(shareId, Integer.valueOf(userId));
                    for (FileShared fileShared : fileShareds) {
                        if (Objects.equals(fileShared.getFileCategory(), FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue())) {
                            deleteShareFiles(fileShared, userId);
                        } else {
                            fileShareMapper.deleteById(fileShared.getShareId());
                        }
                    }
                }
                fileInfoMapper.updateSharedByFileIdAndUserId(fileId, userId, false);
                fileShareMapper.deleteById(shareId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("取消分享失败");
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "取消分享成功");
        return ResponseEntity.ok().body(mp);
    }

    private void deleteShareFiles(FileShared f, String userId) {
        if (Objects.equals(f.getFileCategory(), FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue())) {
            List<FileShared> fileShareds = fileShareMapper.selectShareListBySharePIdAndUserId(f.getShareId(), Integer.valueOf(userId));
            for (FileShared fileShared : fileShareds) {
                if (Objects.equals(fileShared.getFileCategory(), FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue())) {
                    deleteShareFiles(fileShared, userId);
                } else {
                    fileShareMapper.deleteById(fileShared.getShareId());
                }
            }
        }
        fileShareMapper.deleteById(f.getShareId());
    }
}
