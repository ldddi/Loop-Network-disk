package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.DownloadService;
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
public class DownloadServiceImpl implements DownloadService {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<FileSystemResource> download(String fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        FileInfo fileInfo = null;
        try {
            fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, Integer.valueOf(userId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("获取文件失败");
        }

        Path path = Paths.get(fileInfo.getFilePath());
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new MyException("文件不存在");
        }
        FileSystemResource resource = new FileSystemResource(path.toFile());

        HttpHeaders headers = new HttpHeaders();
        // 处理文件名编码，确保文件名中的特殊字符不会导致问题
        String encodedFilename;
        encodedFilename = URLEncoder.encode(fileInfo.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        // 设置 Content-Disposition 头部为附件下载
        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
