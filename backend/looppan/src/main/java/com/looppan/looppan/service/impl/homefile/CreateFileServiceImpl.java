package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.CreateFileService;
import com.looppan.looppan.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateFileServiceImpl implements CreateFileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> createFile(String filePId, String fileName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetail = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetail.getUser();
        Map<String, String> mp = new HashMap<>();
        String random = RandomUtils.generateRandomString(FileStaticKey.FILE_ID_LENGTH.toIntegerValue());

        if (filePId.equals("0")) {
            uploadDir += user.getUserId();
        } else {
            uploadDir = fileInfoMapper.selectById(filePId).getFilePath();
        }
        Path path = Paths.get(uploadDir + "/" + random);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new MyException("文件夹创建失败，请稍后尝试");
        }
        LocalDateTime now = LocalDateTime.now();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(random);
        fileInfo.setUserId(Integer.valueOf(user.getUserId()));
        fileInfo.setFilePid(filePId);
        fileInfo.setFileSize(String.valueOf(0));
        fileInfo.setFileName(fileName);
        fileInfo.setFilePath(uploadDir);
        fileInfo.setCreateTime(now);
        fileInfo.setLastUpdateTime(now);
        fileInfo.setFolderType(FileStaticKey.FILE_TYPE_FOLDER.toIntegerValue());
        fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_FOLDER.toIntegerValue());
        fileInfo.setFileType(FileStaticKey.FILE_TYPE_FOLDER.toIntegerValue());
        fileInfo.setDelFlag(FileStaticKey.DEL_FLAG_NORMAL.toIntegerValue());
        try {
            fileInfoMapper.insert(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.put("message", "文件夹创建成功");
        System.out.println(fileInfoMapper.selectByFileIdAndUserId(random, Integer.valueOf(user.getUserId())));
        return ResponseEntity.ok().body(mp);
    }
}
