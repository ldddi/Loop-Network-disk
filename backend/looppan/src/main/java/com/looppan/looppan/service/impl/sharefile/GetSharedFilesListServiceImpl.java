package com.looppan.looppan.service.impl.sharefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.GetFileListService;
import com.looppan.looppan.service.sharefile.GetSharedFilesListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GetSharedFilesListServiceImpl implements GetSharedFilesListService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<Map> getSharedFilesList(String shareId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        List<FileShared> fileShareds = null;
        if (shareId == null) {
            try {
                fileShareds = fileShareMapper.selectByUserId(Integer.valueOf(userId));
            } catch (Exception e) {
                throw new MyException("获取分享文件记录失败");
            }
        } else {
            Integer idx = Math.max(shareId.lastIndexOf("/"), shareId.lastIndexOf("\\"));
            shareId = shareId.substring(idx + 1);
            try {
                fileShareds = fileShareMapper.selectShareListBySharePIdAndUserId(shareId, Integer.valueOf(userId));
            } catch (Exception e) {
                throw new MyException("获取分享文件记录失败");
            }
        }

        Map<String, Object> mp = new HashMap<>();
        mp.put("data", fileShareds);
        return ResponseEntity.ok().body(mp);
    }
}
