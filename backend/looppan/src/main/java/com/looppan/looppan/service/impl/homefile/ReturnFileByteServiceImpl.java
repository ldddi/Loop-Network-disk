package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.ReturnFileByteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.*;
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
    public ResponseEntity<Object> returnFileByte(String fileId) {
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
        contentDisposition = "inline; filename=\"" + encodedFilename + "\"";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        String contentType;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (Exception e) {
            contentType = "application/octet-stream"; // 默认的二进制流类型
        }

//        if (contentType.substring(0, contentType.lastIndexOf("/")).equals("image")) {
//            // 使用 FFmpeg 处理文件并返回输入流
//            InputStream inputStream = processFileWithFFmpeg(filePath.toString());
//
//            // 创建 InputStreamResource
//            InputStreamResource resource = new InputStreamResource(inputStream);
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .body(resource);
//        }

        // 创建 FileSystemResource
        FileSystemResource resource = new FileSystemResource(filePath.toFile());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }


//    private InputStream processFileWithFFmpeg(String inputFilePath) {
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        processBuilder.command("ffmpeg", "-i", inputFilePath, "-q:v", "20", "-f", "image2pipe", "pipe:1");
//
//        try {
//            Process process = processBuilder.start();
//            return process.getInputStream(); // 返回 FFmpeg 的输出流
//        } catch (IOException e) {
//            throw new RuntimeException("Error processing file with FFmpeg", e);
//        }
//    }
}
