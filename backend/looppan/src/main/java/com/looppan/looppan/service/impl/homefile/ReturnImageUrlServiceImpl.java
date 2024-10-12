package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.ReturnImageUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ReturnImageUrlServiceImpl implements ReturnImageUrlService {

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Resource> returnImageUrl(String fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        FileInfo fileInfo;
        try {
            fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, Integer.valueOf(userId));
        } catch (NumberFormatException e) {
            throw new MyException("获取文件信息失败");
        }

        File file = new File(fileInfo.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        String encodedFilename;
        try {
            encodedFilename = URLEncoder.encode(fileInfo.getFileName(), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + encodedFilename); // inline以便在浏览器中查看
        return ResponseEntity.ok()
                .headers(headers)
//                .contentType(MediaType.IMAGE_JPEG) // 根据实际文件类型设置
                .body(resource);
    }
}
