package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.MoveFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MoveFileServiceImpl implements MoveFileService {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Value("${file.upload-dir}")
    String uploadDir;

    @Override
    @Transactional
    public ResponseEntity<Map> moveFiles(List<String> filesId, String pId) {
        Map<String, String> mp = new HashMap<>();
        mp.put("message", "");
        if (filesId.size() <= 0) return ResponseEntity.ok().body(mp);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();
        String pPath = "";
        if (!Objects.equals(pId, "0")) {
            FileInfo pFileInfo = fileInfoMapper.selectByFileIdAndUserId(pId, Integer.valueOf(userId));
            pPath = pFileInfo.getFilePath();
        } else {
            pPath = uploadDir + "/" + user.getUserId();
        }


        for (int i = 0; i < filesId.size(); i++) {
            FileInfo fileInfo;
            try {
                fileInfo = fileInfoMapper.selectByFileIdAndUserId(filesId.get(i), Integer.valueOf(userId));
                fileInfoMapper.updateByFileIdAndUserId(fileInfo.getFileId(), userId, pId, pPath + "/" + fileInfo.getFileName());
            } catch (Exception e) {
                throw new MyException("更新失败");
            }
            // 源文件路径
            String sourceFilePath = fileInfo.getFilePath();
            // 目标文件路径
            String destinationFilePath = pPath;
            // 创建 Path 对象
            Path sourcePath = Paths.get(sourceFilePath);
            Path destinationPath = Paths.get(destinationFilePath, fileInfo.getFileName());

            try {
                // 移动文件夹
                Files.move(sourcePath, destinationPath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new MyException("移动文件失败");
            }
        }

        mp.put("message", "移动成功");
        return ResponseEntity.ok().body(mp);
    }
}
