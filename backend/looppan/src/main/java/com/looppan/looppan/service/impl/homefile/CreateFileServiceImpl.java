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
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Integer userId = Integer.valueOf(user.getUserId());
        Map<String, Object> mp = new HashMap<>();
        String random = RandomUtils.generateRandomString(FileStaticKey.FILE_ID_LENGTH.toIntegerValue());

        List<FileInfo> curFolder = fileInfoMapper.selectByFilePidAndUserIdAndLimitType(filePId,userId, FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue());
        for (FileInfo fileInfo : curFolder) {
            if (Objects.equals(fileInfo.getFileName(), fileName)) {
                throw new MyException("当前文件夹已存在");
            }
        }

        if (filePId == null || filePId.equals("0") ) {
            uploadDir += user.getUserId();
        } else {
            try {
                filePId = filePId.substring(filePId.lastIndexOf("/") + 1);
                uploadDir = fileInfoMapper.selectByFileIdAndUserId(filePId, Integer.valueOf(user.getUserId())).getFilePath();
            } catch (Exception e) {
                throw new MyException("文件夹创建失败, 请稍后尝试");
            }
        }
        Path path = Paths.get(uploadDir + "/" + random);
        System.out.println(path);
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
        fileInfo.setFilePath(uploadDir + "/" + random);
        fileInfo.setCreateTime(now);
        fileInfo.setLastUpdateTime(now);
        fileInfo.setFolderType(FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue());
        fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_FOLDER.toIntegerValue());
        fileInfo.setDelFlag(FileStaticKey.DEL_FLAG_NORMAL.toIntegerValue());

        try {
            fileInfoMapper.insert(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.put("message", "文件夹创建成功");
        mp.put("data", fileInfo);
        System.out.println(fileInfoMapper.selectByFileIdAndUserId(random, Integer.valueOf(user.getUserId())));
        return ResponseEntity.ok().body(mp);
    }
}
