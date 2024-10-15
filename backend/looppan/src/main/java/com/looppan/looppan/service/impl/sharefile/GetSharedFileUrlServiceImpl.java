package com.looppan.looppan.service.impl.sharefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.sharefile.GetSharedFileUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetSharedFileUrlServiceImpl implements GetSharedFileUrlService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<Map> getSharedFileUrl(String shareId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        FileShared fileShared = null;
        try {
            fileShared = fileShareMapper.selectById(shareId);
        } catch (Exception e) {
            throw new MyException("获取分享文件链接失败");
        }

        String url = fileShared.getFileUrl();
        String code = fileShared.getExtractionCode();

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "复制链接成功");
        mp.put("data", "链接: " + url + " 提取码=" + code);

        return ResponseEntity.ok().body(mp);
    }
}
