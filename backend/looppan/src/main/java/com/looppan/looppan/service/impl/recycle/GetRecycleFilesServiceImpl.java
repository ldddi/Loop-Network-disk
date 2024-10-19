package com.looppan.looppan.service.impl.recycle;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.recycle.GetRecycleFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetRecycleFilesServiceImpl implements GetRecycleFilesService {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> getRecycleFiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        List<FileInfo> fileInfos = null;

        try {
            fileInfos = fileInfoMapper.selectRecycleFilesByUserId(Integer.valueOf(userId), FileStaticKey.DEL_FLAG_RECOVERY.toIntegerValue());
        } catch (NumberFormatException e) {
            throw new MyException("获取分享文件列表失败");
        }

        Map<String, Object> mp = new HashMap<>();
        mp.put("data", fileInfos);
        return ResponseEntity.ok().body(mp);
    }
}
