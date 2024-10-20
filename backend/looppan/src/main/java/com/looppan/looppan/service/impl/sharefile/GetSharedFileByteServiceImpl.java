package com.looppan.looppan.service.impl.sharefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.sharefile.GetSharedFileByteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GetSharedFileByteServiceImpl implements GetSharedFileByteService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<FileSystemResource> getSharedFile(String fileId) {
        // 获取当前认证的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();
        String shareId = fileId + userId;
        // 查询文件信息
        FileShared fileShared;
        try {
            fileShared = fileShareMapper.selectById(shareId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("获取文件信息失败");
        }

        if (fileShared == null) {
            throw new MyException("文件信息不存在");
        }

        Path filePath = Paths.get(fileShared.getFilePath());

        if (!Files.exists(filePath)) {

            throw new MyException("文件不存在");
        }

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        String encodedFilename;
        try {
            encodedFilename = URLEncoder.encode(fileShared.getFileName(), StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // 根据文件类别选择 Content-Disposition 和 Content-Type
        String contentDisposition;
        contentDisposition = "inline; filename=\"" + encodedFilename + "\"";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        String contentType;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (Exception e) {
            contentType = "application/octet-stream"; // 默认的二进制流类型
        }

        // 创建 FileSystemResource
        FileSystemResource resource = new FileSystemResource(filePath.toFile());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
