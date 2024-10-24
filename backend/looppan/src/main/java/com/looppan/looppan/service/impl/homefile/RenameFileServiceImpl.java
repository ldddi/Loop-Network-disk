package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.RenameFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class RenameFileServiceImpl implements RenameFileService {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> renameFile(String fileId, String newName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();


        FileInfo fileInfo = null;

        fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, Integer.valueOf(userId));
        String fileName = fileInfo.getFileName();
        if (Objects.equals(fileInfo.getFolderType(), FileStaticKey.FOLDER_TYPE_FILE.toIntegerValue())) {
            newName = newName + fileName.substring(fileName.lastIndexOf("."));
        }
        String filePath = fileInfo.getFilePath();
        int index = filePath.lastIndexOf(fileName);
        String newPath = "";
        if (index >= 0) {
            newPath = filePath.substring(0, index) + newName;
        } else {
            throw new MyException("重命名失败");
        }
        fileInfoMapper.updateFileNameAndPathByFileIdAndUserId(fileId, userId, newName, newPath);

        Path sourcePath = Paths.get(filePath);
        Path targetPath = Paths.get(newPath);

        try {
            Files.move(sourcePath, targetPath);
        } catch (IOException e) {
            throw new MyException("重命名失败");
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "重命名成功");
        mp.put("data", newName);
        return ResponseEntity.ok().body(mp);
    }
}
