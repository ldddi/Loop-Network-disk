package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.utils.RandomUtils;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.UpdateUserInfoService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UpdateUserInfoServiceImpl implements UpdateUserInfoService {
    @Autowired
    UserMapper userMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.avatar}")
    private String myAvatar;

    @Override
    public ResponseEntity<Map> updateUserInfo(String nickName, MultipartFile avatar) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();
        Map<String, Object> mp = new HashMap<>();

        if (nickName != null) {
            user.setNickName(nickName);
            mp.put("nickName", nickName);
        }

        if (avatar != null && !avatar.isEmpty()) {
            Path basePath = Paths.get(uploadDir, userId, myAvatar);
            if (!Files.exists(basePath)) {
                Files.createDirectory(basePath);
            }

            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(basePath);
            for (Path file : directoryStream) {
                Files.delete(file);
            }

            String fileName = avatar.getOriginalFilename();
            Path destPath = basePath.resolve(fileName);
            avatar.transferTo(destPath);
            user.setAvatar(destPath.toString());

            mp.put("avatar", destPath.toString());
        }
        userMapper.updateById(user);

        mp.put("message", "用户信息修改成功");
        return ResponseEntity.ok().body(mp);
    }
}
