package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.GetAllCategoryFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetAllCategoryFileServiceImpl implements GetAllCategoryFileService {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> getAllAudioFiles(Integer category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();
        List<FileInfo> fileInfos = fileInfoMapper.selectAllFilesByUserId(Integer.valueOf(userId), category);
        Map<String, Object> mp = new HashMap<>();
        mp.put("data", fileInfos);

        return ResponseEntity.ok().body(mp);
    }
}
