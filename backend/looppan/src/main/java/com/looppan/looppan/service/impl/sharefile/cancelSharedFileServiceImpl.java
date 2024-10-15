package com.looppan.looppan.service.impl.sharefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.sharefile.CancelSharedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class cancelSharedFileServiceImpl implements CancelSharedFileService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<Map> cancelSharedFile(String shareId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        try {
            fileShareMapper.deleteById(shareId);
        } catch (Exception e) {
            throw new MyException("取消分享失败");
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "取消分享成功");
        return ResponseEntity.ok().body(mp);
    }
}
