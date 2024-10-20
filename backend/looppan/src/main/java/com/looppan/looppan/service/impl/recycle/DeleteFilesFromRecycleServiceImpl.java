package com.looppan.looppan.service.impl.recycle;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.recycle.DeleteFilesFromRecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DeleteFilesFromRecycleServiceImpl implements DeleteFilesFromRecycleService {

    @Autowired
    FileInfoMapper fileInfoMapper;
    @Autowired
    private FileShareMapper fileShareMapper;

    @Override
    public ResponseEntity<Map> deleteFilesFromRecycleService(List<String> filesId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();

        for (int i = 0; i < filesId.size(); i++) {
            FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(filesId.get(i), Integer.valueOf(userId));
            try {
                if (Objects.equals(FileStaticKey.FOLDER_TYPE_FILE.toIntegerValue(), fileInfo.getFolderType())) {
                    Path filePath = Paths.get(fileInfo.getFilePath());
                    Files.delete(filePath);
                } else {
                    File directory = new File(fileInfo.getFilePath());
                    File[] files = directory.listFiles();
                    for (File file : files) {
                        if (file.isDirectory()) {
                            deleteDirectory(file);
                        } else {
                            Files.delete(file.toPath());
                        }
                    }
                    directory.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException("删除系统文件失败");
            }
        }

        try {
            for (String fileId : filesId) {
                List<FileInfo> fileInfos = fileInfoMapper.selectByFilePidAndUserId(fileId, Integer.valueOf(userId));
                for (FileInfo fileInfo : fileInfos) {
                    if (Objects.equals(fileInfo.getFolderType(), FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue())) {
                        deleteFileinfo(fileInfo, userId);
                    } else {
                        fileInfoMapper.DeleteByFileIdAndUserId(fileInfo.getFileId(), Integer.valueOf(userId));
                    }
                }
                FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, Integer.valueOf(userId));
                if (fileInfo.getShared()) {
                    String shareId = fileInfo.getFileId() + userId;
                    fileShareMapper.deleteById(shareId);
                }
                fileInfoMapper.DeleteByFileIdAndUserId(fileId, Integer.valueOf(userId));
            }
        } catch (RuntimeException e) {
            throw new MyException("删除失败");
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "删除成功");
        return ResponseEntity.ok().body(mp);
    }

    private void deleteFileinfo(FileInfo folder, String userId) {
        List<FileInfo> fileInfos = fileInfoMapper.selectByFilePidAndUserId(folder.getFileId(), Integer.valueOf(userId));
        for (FileInfo fileInfo : fileInfos) {
            if (fileInfo.getFolderType() == FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue()) {
                deleteFileinfo(fileInfo, userId);
            } else {
                fileInfoMapper.DeleteByFileIdAndUserId(fileInfo.getFileId(), Integer.valueOf(userId));
            }
        }
        fileInfoMapper.DeleteByFileIdAndUserId(folder.getFileId(), Integer.valueOf(userId));
    }

    // 递归删除目录的方法
    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file); // 递归删除
                } else {
                    file.delete(); // 删除文件
                }
            }
        }
        dir.delete(); // 删除空目录
    }
}
