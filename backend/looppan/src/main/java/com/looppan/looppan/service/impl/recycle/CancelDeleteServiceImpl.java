package com.looppan.looppan.service.impl.recycle;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.recycle.CancelDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CancelDeleteServiceImpl implements CancelDeleteService {

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> cancelDelete(List<String> filesId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        try {
            for (String fileId : filesId) {
                FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, Integer.valueOf(userId));

                Files.move(Path.of(fileInfo.getFilePath()), Path.of(fileInfo.getFileLastPath()));

                LocalDateTime now = LocalDateTime.now();
                fileInfoMapper.updateDelFlagByFileIdAndUserId(
                        fileId,
                        Integer.valueOf(userId),
                        FileStaticKey.DEL_FLAG_NORMAL.toIntegerValue(),
                        now,
                        fileInfo.getFileLastPath(),
                        fileInfo.getFilePath()
                        );
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("还原失败");
        }
        Map<String, String> mp = new HashMap<>();
        mp.put("message", "还原成功");
        return ResponseEntity.ok().body(mp);
    }
}
