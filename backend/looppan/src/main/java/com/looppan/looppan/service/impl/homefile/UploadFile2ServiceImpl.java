package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.UploadFile2Service;
import com.looppan.looppan.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
public class UploadFile2ServiceImpl implements UploadFile2Service {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> upload(String filePId, MultipartFile fileChunk, String fileName, String fileSize, Integer index, Integer totalChunks) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();
        FileInfo fileInfo;
        List<FileInfo> fileInfoList = null;

        Path basePath = Paths.get(uploadDir + "/" + userId);
        if (!Objects.equals(filePId, "0")) {
            try {
                fileInfo = fileInfoMapper.selectByFileIdAndUserId(filePId, Integer.valueOf(userId));
                basePath = Paths.get(fileInfo.getFilePath());
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }

        fileInfoList = fileInfoMapper.selectByFilePidAndUserIdAndLimitType(filePId, Integer.valueOf(userId), FileStaticKey.FOLDER_TYPE_FILE.toIntegerValue());
        if (fileInfoList != null) {
            for (FileInfo fileInfo1 : fileInfoList) {
                if (Objects.equals(fileInfo1.getFileName(), fileName)) {
                    throw new MyException("文件名已存在");
                }
            }
        }

        Path uploadPath = basePath.resolve(fileName + ".part" + index);
        fileChunk.transferTo(uploadPath.toFile());

        if (Objects.equals(index, totalChunks - 1)) {
            mergeChunks(basePath ,fileName, totalChunks);
            String filePath = basePath.resolve(fileName).toString();
            FileInfo dataFileInfo = insertToFileInfo(fileName, fileSize, filePId,filePath, userId ,fileChunk.getContentType());

            Map<String, Object> mp = new HashMap<>();
            mp.put("message", "文件上传成功");
            mp.put("data", dataFileInfo);
            return ResponseEntity.ok().body(mp);
        }

        Map<String, String> mp = new HashMap<>();
        return ResponseEntity.ok().body(mp);
    }

    private void mergeChunks(Path basePath ,String fileName, Integer totalChunks) throws IOException {
        Path mergedPath = basePath.resolve(fileName);

        try (FileOutputStream fos = new FileOutputStream(mergedPath.toFile())) {
            for (int i = 0; i < totalChunks; i ++) {
                Path partPath = basePath.resolve(fileName + ".part" + i);
                if (Files.exists(partPath)) {
                    Files.copy(partPath, fos);
                    Files.delete(partPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("上传失败");
        }
    }

    private FileInfo insertToFileInfo(String fileName, String fileSize, String filePId, String filePath, String userId ,String contentType) {
        FileInfo fileInfo = new FileInfo();
        String fileId = RandomUtils.generateRandomString(FileStaticKey.FILE_ID_LENGTH.toIntegerValue());
        LocalDateTime now = LocalDateTime.now();

        fileInfo.setFileId(fileId);
        fileInfo.setUserId(Integer.valueOf(userId));
        fileInfo.setFilePid(filePId);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileName(fileName);
        fileInfo.setFilePath(filePath);
        fileInfo.setCreateTime(now);
        fileInfo.setLastUpdateTime(now);
        fileInfo.setFolderType(FileStaticKey.FOLDER_TYPE_FILE.toIntegerValue());

        String extension = fileName != null ? fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase() : "";
        if (contentType.startsWith("image/") || extension.matches("(?i)(jpg|jpeg|png|gif)")) {
            fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_IMAGE.toIntegerValue());
        } else if (contentType.startsWith("audio/") || extension.matches("(?i)(mp3|wav|aac|m4a)")) {
            fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_AUDIO.toIntegerValue());
        } else if (contentType.startsWith("video/") || extension.matches("(?i)(mp4|avi|mov)")) {
            fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_VIDEO.toIntegerValue());
        } else if (contentType.startsWith("application/") || extension.matches("(?i)(pdf|doc|docx|xls|xlsx)")) {
            fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_DOC.toIntegerValue());
        } else {
            fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_OTHER.toIntegerValue());
        }

        try {
            fileInfoMapper.insert(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("文件上传失败");
        }
        System.out.println(fileInfo);
        return fileInfo;
    }
}
