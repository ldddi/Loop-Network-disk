package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.user.utils.RandomUtils;
import com.looppan.looppan.controller.user.utils.StaticKey;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.UpdateUserInfoService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
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
        Map<String, String> mp = new HashMap<>();

        if (nickName != null) {
            user.setNickName(nickName);
            mp.put("nickName", nickName);
        }

        if (avatar != null && !avatar.isEmpty()) {
            // 读取文件为字节数组
            byte[] imageData = avatar.getBytes();

            // 将文件数据进行 Base64 编码
            String encodedImage = Base64.getEncoder().encodeToString(imageData);

            LocalDateTime now = LocalDateTime.now();
            String str = String.valueOf(now.getYear()) + String.valueOf(now.getMonthValue()) + RandomUtils.generateRandomString(StaticKey.AVATAR_UUID_LENGTH.toIntegerValue());
            String extension = Objects.requireNonNull(avatar.getOriginalFilename()).substring(avatar.getOriginalFilename().lastIndexOf('.'));

            // 构建 GitHub 上传路径和请求体
            String repo = "cheng-rainbow/myimage"; // 替换为你的 GitHub 用户名和仓库名
            String path = "images/" + str + extension; // 目标路径

            String json = String.format(
                    "{\"message\": \"Upload avatar\", \"content\": \"%s\"}",
                    encodedImage
            );

            // 设置请求头
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url("https://api.github.com/repos/" + repo + "/contents/" + path)
                    .put(body)
                    .addHeader("Authorization", "token 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") // 替换为你的 GitHub 令牌
                    .addHeader("Content-Type", "application/json")
                    .build();

            // 执行请求
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new MyException("头像上传失败, 请稍后尝试!");
                user.setAvatar("https://raw.githubusercontent.com/cheng-rainbow/myimage/main/images/" + str + extension);
//                System.out.println("Avatar uploaded successfully: " + response.body().string());
                mp.put("avatar", user.getAvatar());
            }
        }
        userMapper.updateById(user);

        mp.put("message", "用户信息修改成功");
        return ResponseEntity.ok().body(mp);
    }
}
