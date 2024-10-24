package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.mapper.UserMapper;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

@Service
public class UploadFile2ServiceImpl implements UploadFile2Service {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional
    public ResponseEntity<Map> upload(String filePId, MultipartFile fileChunk, String fileName, String fileSize, Integer index, Integer totalChunks) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String userId = user.getUserId();
        FileInfo fileInfo;
        List<FileInfo> fileInfoList = null;

        Path basePath = Paths.get(uploadDir + "/" + userId);
        if (!Objects.equals(filePId, "0")) {
            filePId = filePId.substring(filePId.lastIndexOf("/") + 1);
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

        // 合并
        if (Objects.equals(index, totalChunks - 1)) {
            long size = mergeChunks(basePath ,fileName, totalChunks, userId);

            System.out.println(size);

            if (size == 0) {
                throw new MyException("文件超出所剩容量");
            }

            Path filePath = basePath.resolve(fileName);
            FileInfo dataFileInfo = insertToFileInfo(fileName, String.valueOf(size), filePId,filePath.toString(), userId ,fileChunk.getContentType());

            if (Objects.equals(dataFileInfo.getFileCategory(), FileStaticKey.FILE_CATEGORY_IMAGE.toIntegerValue())) {
                boolean flag = createImageCover(userId, fileName, dataFileInfo);
                if (!flag) {
                    fileInfoMapper.deleteById(dataFileInfo);
                    Files.deleteIfExists(filePath);
                    throw new MyException("上传失败, 检查文件是否受损");
                }
            }

            Map<String, Object> mp = new HashMap<>();
            mp.put("message", "文件上传成功");
            mp.put("data", dataFileInfo);
            return ResponseEntity.ok().body(mp);
        }

        Map<String, String> mp = new HashMap<>();
        return ResponseEntity.ok().body(mp);
    }

    private long mergeChunks(Path basePath ,String fileName, Integer totalChunks, String userId) throws IOException {
        Path mergedPath = basePath.resolve(fileName);
        long fileSize = 0;
        try (FileOutputStream fos = new FileOutputStream(mergedPath.toFile())) {
            for (int i = 0; i < totalChunks; i ++) {
                Path partPath = basePath.resolve(fileName + ".part" + i);
                fileSize += Files.size(partPath);
                if (Files.exists(partPath)) {
                    Files.copy(partPath, fos);
                    Files.delete(partPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("上传失败");
        }
        User user = userMapper.selectByIdForUpdate(userId);
        BigInteger useSpace = user.getUseSpace();
        BigInteger totalSpace = user.getTotalSpace();
        System.out.println(fileName + " " + useSpace);
        if (useSpace.add(BigInteger.valueOf(fileSize)).compareTo(totalSpace) > 0) {
            Files.delete(mergedPath);
            return 0;
        } else {
            useSpace = useSpace.add(BigInteger.valueOf(fileSize));
            System.out.println(fileName + " " + useSpace);
            user.setUseSpace(useSpace);
            userMapper.updateById(user);
            return fileSize;
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
        return fileInfo;
    }

    private boolean createImageCover (String userId, String fileName, FileInfo dataFileInfo) throws IOException {
        Path basePath = Paths.get(uploadDir);
        Path coverPath = basePath.resolve(userId).resolve("cover");

        if (!Files.exists(coverPath)) {
            Files.createDirectories(coverPath);
        }
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        String random = RandomUtils.generateRandomString(FileStaticKey.FILE_ID_LENGTH.toIntegerValue());
        random += extension;
        Path outputFilePath = coverPath.resolve(random);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("ffmpeg", "-i", dataFileInfo.getFilePath(), "-q:v", "20", outputFilePath.toString());

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor(); // 等待 FFmpeg 进程完成
            if (exitCode != 0) {
                return false;
            }

            dataFileInfo.setFileCover(outputFilePath.toString());
            fileInfoMapper.updateById(dataFileInfo);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error processing file with FFmpeg", e);
        }
        return true;
    }
}
