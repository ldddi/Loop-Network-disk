package com.looppan.looppan.service.impl.recycle;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.recycle.DeleteFilesFromRecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class DeleteFilesFromRecycleServiceImpl implements DeleteFilesFromRecycleService {

    @Autowired
    FileInfoMapper fileInfoMapper;
    @Autowired
    private FileShareMapper fileShareMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseEntity<Map> deleteFilesFromRecycleService(List<String> filesId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();
        System.out.println("deleteFilesFromRecycleService");
        BigInteger size = BigInteger.valueOf(0);
        for (int i = 0; i < filesId.size(); i++) {
            FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(filesId.get(i), Integer.valueOf(userId));
            try {
                if (Objects.equals(FileStaticKey.FOLDER_TYPE_FILE.toIntegerValue(), fileInfo.getFolderType())) {
                    Path filePath = Paths.get(fileInfo.getFilePath());
                    if(Files.exists(filePath)) {
                        size = size.add(BigInteger.valueOf(Files.size(filePath)));
                        Files.deleteIfExists(filePath);
                    }

                } else {
                    Path dir = Paths.get(fileInfo.getFilePath());
                    List<Path> list = Files.list(dir).toList();
                    for (Path path : list) {
                        if (Files.isDirectory(path)) {
                            size = size.add(deleteDirectory(path));
                        } else {
                            size = size.add(BigInteger.valueOf(Files.size(path)));
                            Files.deleteIfExists(path);
                        }
                    }
                    Files.deleteIfExists(dir);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException("删除系统文件失败");
            }
        }

        BigInteger useSpace = user.getUseSpace();
        useSpace = useSpace.subtract(size);
        user.setUseSpace(useSpace);
        userMapper.updateById(user);

        try {
            for (String fileId : filesId) {
                // 如果删除的是目录, 删除它的子文件
                List<FileInfo> fileInfos = fileInfoMapper.selectByFilePidAndUserId(fileId, Integer.valueOf(userId));
                for (int i = 0; i < fileInfos.size(); i++) {
                    FileInfo fileInfo = fileInfos.get(i);
                    if (Objects.equals(fileInfo.getFolderType(), FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue())) {
                        deleteFileinfo(fileInfo, userId);
                    } else {
                        if (fileInfo.getFileCover() != null) {
                            Path coverPath = Paths.get(fileInfo.getFileCover());
                            Files.deleteIfExists(coverPath);
                        }
                        fileInfoMapper.DeleteByFileIdAndUserId(fileInfo.getFileId(), Integer.valueOf(userId));
                    }
                }

                FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, Integer.valueOf(userId));
                if (fileInfo.getShared()) {
                    String shareId = fileInfo.getFileId() + userId;
                    fileShareMapper.deleteById(shareId);
                }
                if (fileInfo.getFileCover() != null) {
                    Path coverPath = Paths.get(fileInfo.getFileCover());
                    if (Files.exists(coverPath)) {
                        Files.delete(coverPath);
                    }

                }
                fileInfoMapper.DeleteByFileIdAndUserId(fileId, Integer.valueOf(userId));
            }
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            throw new MyException("删除失败");
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "删除成功");

        return ResponseEntity.ok().body(mp);
    }
    private void deleteFileinfo(FileInfo folder, String userId) throws IOException {
        List<FileInfo> fileInfos = fileInfoMapper.selectByFilePidAndUserId(folder.getFileId(), Integer.valueOf(userId));
        for (FileInfo fileInfo : fileInfos) {
            if (fileInfo.getFolderType() == FileStaticKey.FOLDER_TYPE_FOLDER.toIntegerValue()) {
                deleteFileinfo(fileInfo, userId);
            } else {
                if (fileInfo.getFileCover() != null) {
                    Path coverPath = Paths.get(fileInfo.getFileCover());
                    Files.deleteIfExists(coverPath);
                }
                fileInfoMapper.DeleteByFileIdAndUserId(fileInfo.getFileId(), Integer.valueOf(userId));
            }
        }
        if (folder.getFileCover() != null) {
            Path coverPath = Paths.get(folder.getFileCover());
            if (Files.exists(coverPath)) {
                Files.delete(coverPath);
            }
        }

        fileInfoMapper.DeleteByFileIdAndUserId(folder.getFileId(), Integer.valueOf(userId));
    }

    // 递归删除目录的方法
    private BigInteger deleteDirectory(Path dir) throws IOException {

        BigInteger size = BigInteger.valueOf(0);
        List<Path> list = Files.list(dir).toList();
        for (Path path : list) {
            if (Files.isDirectory(path)) {
                size = size.add(deleteDirectory(path));
            } else {
                size = size.add(BigInteger.valueOf(Files.size(path)));
                Files.deleteIfExists(path);
            }
        }
        Files.deleteIfExists(dir);
        return size;
    }
}
