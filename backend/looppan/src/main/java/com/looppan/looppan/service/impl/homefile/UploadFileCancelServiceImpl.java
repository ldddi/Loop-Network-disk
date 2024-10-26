package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.UploadFileCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class UploadFileCancelServiceImpl implements UploadFileCancelService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> uploadFileCancel(String filePId, String fileName) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        Path basePath = Paths.get(uploadDir, userId);
        FileInfo fileInfo;

        if (filePId != null && !filePId.equals("0")) {
            try {
                fileInfo = fileInfoMapper.selectByFileIdAndUserId(filePId, Integer.valueOf(userId));
            } catch (NumberFormatException e) {
                throw new MyException("获取文件信息失败");
            }
            String path = fileInfo.getFilePath();
            basePath = Paths.get(path);
        }

        if (Files.isDirectory(basePath)) {
            if (filePId != null) {
                deletePartFile1(basePath, fileName);
            } else {
                deletePartFile2(basePath);
            }
        }
        Map<String, String> mp = new HashMap<>();
        if (filePId != null && fileName != null) {
            mp.put("message", "取消上传成功");
        }
        return ResponseEntity.ok().body(mp);
    }

    // 删除 fileName 的 part
    private void deletePartFile1(Path basePath, String fileName) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(basePath, path -> {
            String name = path.getFileName().toString();
            String pattern = fileName + "\\.part\\d+";
            return name.matches(pattern);
        })) {
            for (Path file : directoryStream) {
                Files.delete(file);
            }
        }
    }

    // 删除全部的 part
    private void deletePartFile2(Path basePath) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(basePath, path -> {
            String fileName = path.getFileName().toString();
            return Files.isDirectory(path) || fileName.matches(".*\\.part\\d+");
        })) {
            for (Path file : directoryStream) {
                if (Files.isDirectory(file)) {
                    deletePartFile2(file);
                } else {
                    Files.delete(file);
                }
            }
        }
    }
}
