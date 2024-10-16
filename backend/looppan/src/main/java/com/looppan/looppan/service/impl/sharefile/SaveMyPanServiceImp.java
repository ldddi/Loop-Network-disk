package com.looppan.looppan.service.impl.sharefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.sharefile.SaveMyPanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaveMyPanServiceImp implements SaveMyPanService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Autowired
    UserMapper userMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public ResponseEntity<Map> saveMyPan(String shareId, String userId) {
        FileShared fileShared = null;
        User user = null;
        try {
            user = userMapper.selectById(userId);
            fileShared = fileShareMapper.selectById(shareId);
        } catch (Exception e) {
            throw new MyException("获取分享文件失败");
        }

        Path path = Paths.get(fileShared.getFilePath());
        if (!Files.exists(path)) {
            throw new MyException("文件以失效");
        }

        String dist = uploadDir + "/" + user.getUserId() + "/" + fileShared.getFileName();
        Path distPath = Paths.get(dist);

        try {
            Files.createDirectories(distPath.getParent());

            Files.copy(path, distPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException("保存失败");
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "保存成功");
        return ResponseEntity.ok().body(mp);
    }
}
