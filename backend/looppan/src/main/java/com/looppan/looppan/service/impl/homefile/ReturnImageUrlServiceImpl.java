package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.ReturnImageUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class ReturnImageUrlServiceImpl implements ReturnImageUrlService {

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<String> returnImageUrl(String fileId) {
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

//        Path file = Paths.get(fileInfo.getFilePath());
//        Resource resource = null;
//        try {
//            resource = new UrlResource(file.toUri());
//        } catch (MalformedURLException e) {
//            throw new MyException("resource 失败");
//        }
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
        // 构建可以直接访问的 URL
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileInfo.getFileName())
                .toUriString();

        return ResponseEntity.ok(fileUrl);
    }
}
