package com.looppan.looppan.service.impl.user;

import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.user.GetUserAvatarByteService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GetUserAvatarByteServiceImpl implements GetUserAvatarByteService {
    @Override
    public ResponseEntity<FileSystemResource> getUserAvatarByte(String avatarPath) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        Path path = Paths.get(avatarPath);
        FileSystemResource resource = null;
        if (Files.exists(path)) {
            resource =  new FileSystemResource(path);
        }

        HttpHeaders headers = new HttpHeaders();
        // 处理文件名编码，确保文件名中的特殊字符不会导致问题
        String encodedFilename;
        encodedFilename = URLEncoder.encode(String.valueOf(path.getFileName()), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String contentDisposition = "inline; filename=\"" + encodedFilename + "\"";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        String contentType = Files.probeContentType(path);

        return ResponseEntity.ok()
                            .headers(headers)
                            .contentType(MediaType.parseMediaType(contentType))
                            .body(resource);
    }
}
