package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.DeleteSelectedFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DeleteSelectedFileServiceImpl implements DeleteSelectedFilesService {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    @Transactional
    public ResponseEntity<Map> deleteFiles(List<String> filesId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        try {
            for (String fileId : filesId) {
                LocalDateTime now = LocalDateTime.now();
                fileInfoMapper.updateDelFlagByFileIdAndUserId(fileId, Integer.valueOf(userId), FileStaticKey.DEL_FLAG_RECOVERY.toIntegerValue(),now);
            }
        } catch (RuntimeException e) {
            throw new MyException("删除失败");
        }

        Map<String, String> mp = new HashMap<String, String>();
        mp.put("message", "删除成功, 将在回收站保存10天");
        return ResponseEntity.ok().body(mp);
    }
}
