package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.user.utils.RandomUtils;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.UpdateUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UpdateUserInfoServiceImpl implements UpdateUserInfoService {
    @Autowired
    UserMapper userMapper;

    @Override
    public ResponseEntity<Map> updateUserInfo(String nickName, MultipartFile avatar) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (nickName != null) {
            user.setNickName(nickName);
        }

        if (avatar != null && !avatar.isEmpty()) {
            String str = RandomUtils.generateRandomString(1);
            String extension = Objects.requireNonNull(avatar.getOriginalFilename()).substring(avatar.getOriginalFilename().lastIndexOf('.'));

            avatar.transferTo(new File(""));
            user.setAvatar("");
        }
        userMapper.updateById(user);
        Map<String, String> mp = new HashMap<>();
        mp.put("message", "用户信息修改成功");
        return ResponseEntity.ok().body(mp);
    }
}
