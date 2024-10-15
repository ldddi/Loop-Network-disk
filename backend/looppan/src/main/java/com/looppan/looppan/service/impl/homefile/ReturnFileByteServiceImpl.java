package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.ReturnFileByteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ReturnFileByteServiceImpl implements ReturnFileByteService {

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<FileSystemResource> returnFileByte(String fileId) {
        // 获取当前认证的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        // 查询文件信息
        FileInfo fileInfo;
        try {
            fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, Integer.valueOf(userId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("获取文件信息失败");
        }

        if (fileInfo == null) {
            throw new MyException("文件信息不存在");
        }

        Path filePath = Paths.get(fileInfo.getFilePath());
        if (!Files.exists(filePath)) {
            throw new MyException("文件不存在");
        }

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        String encodedFilename;
        try {
            encodedFilename = URLEncoder.encode(fileInfo.getFileName(), StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // 根据文件类别选择 Content-Disposition 和 Content-Type
        String contentDisposition;
        MediaType mediaType = getMediaType(fileInfo.getFileCategory());

        contentDisposition = "inline; filename=\"" + encodedFilename + "\"";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        // 创建 FileSystemResource
        FileSystemResource resource = new FileSystemResource(filePath.toFile());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .body(resource);
    }

    private MediaType getMediaType(Integer fileCategory) {
        switch (fileCategory) {
            case 1:
                return MediaType.valueOf("video/mp4"); // 根据实际情况调整
            case 2:
                return MediaType.valueOf("audio/aac");
            case 3:
                return MediaType.IMAGE_JPEG; // 示例
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
