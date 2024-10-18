package com.looppan.looppan.service.impl.sharefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.service.sharefile.DownSharedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class DownSharedFileServiceImpl implements DownSharedFileService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<FileSystemResource> downFile(String shareId) {
        FileShared fileShared = null;
        try {
            fileShared = fileShareMapper.selectById(shareId);
        } catch (Exception e) {
            throw new MyException("下载文件失败");
        }

        Path path = Paths.get(fileShared.getFilePath());
        if (!Files.exists(path)) {
            throw new MyException("文件已失效");
        }

        FileSystemResource resource = new FileSystemResource(path.toFile());
        String encodedFilename;
        encodedFilename = URLEncoder.encode(fileShared.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        HttpHeaders headers = new HttpHeaders();
        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"";
        // 自动推断Content-Type
        String contentType;
        try {
            contentType = Files.probeContentType(path);
        } catch (Exception e) {
            contentType = "application/octet-stream"; // 默认的二进制流类型
        }

        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
