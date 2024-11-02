package com.looppan.looppan.service.impl.sharefile;

import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;
import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.mapper.FileShareMapper;
import com.looppan.looppan.mapper.UserMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.FileShared;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.sharefile.SaveMyPanService;
import com.looppan.looppan.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class SaveMyPanServiceImp implements SaveMyPanService {

    @Autowired
    FileShareMapper fileShareMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.cover}")
    private String cover;

    @Override
    @Transactional
    public ResponseEntity<Map> saveMyPan(String shareId, String userId) throws IOException {
        FileShared fileShared = null;
        User saveUser = null;
        try {
            saveUser = userMapper.selectById(userId);
            fileShared = fileShareMapper.selectById(shareId);
        } catch (Exception e) {
            throw new MyException("获取分享文件失败");
        }

        if (fileShared == null) {
            throw new MyException("文件已经失效");
        }

        if (saveUser == null) {
            throw new MyException("保存失败,请先登录账号");
        }

        LocalDateTime shareTime = fileShared.getShareTime();
        LocalDateTime failTime = fileShared.getFailTime();
        if (shareTime.isAfter(failTime)) {
            throw new MyException("文件已经失效");
        }

        String sharedUserId = String.valueOf(fileShared.getUserId());
        if (sharedUserId.equals(userId)) {
            throw new MyException("不可以保存自己分享的文件");
        }

        Path source = Paths.get(fileShared.getFilePath());
        if (!Files.exists(source)) {
            throw new MyException("文件已经失效");
        }

        List<FileInfo> fileInfos = fileInfoMapper.selectByFilePidAndUserId2("0", Integer.valueOf(userId));
        for (FileInfo fileInfo : fileInfos) {
            if (Objects.equals(fileInfo.getFileId(), fileShared.getFileId())) {
                throw new MyException("不可以再次保存");
            } else if (Objects.equals(fileInfo.getFileName(), fileShared.getFileName())) {
                throw new MyException("文件名冲突, 请前往首页查看根目录文件名");
            }
        }
        BigInteger size = BigInteger.valueOf(0);
        List<Path> walk = Files.walk(source).toList();
        for (Path path : walk) {
            if (!Files.isDirectory(path)) {
                size = size.add(BigInteger.valueOf(Files.size(path)));
            }
        }
        BigInteger useSpace = saveUser.getUseSpace();
        BigInteger totalSpace = saveUser.getTotalSpace();

        if (size.compareTo(totalSpace.subtract(useSpace)) > 0) {
            throw new MyException("空间不足");
        } else {
            useSpace = useSpace.add(size);
            saveUser.setUseSpace(useSpace);

            userMapper.updateById(saveUser);

        }

        Path basePath = Paths.get(uploadDir);
        Path sourcePrefix = basePath.resolve(String.valueOf(fileShared.getUserId()));
        Path distPrefix = basePath.resolve(saveUser.getUserId());


        try {
//            List<Path> walk = Files.walk(source).toList();
            for (Path sourcePath : walk) {
                if (Files.isDirectory((sourcePath))) {
                    String path = sourcePath.toAbsolutePath().toString();
                    Path distPath = Paths.get(path.replace(sourcePrefix.toString(), distPrefix.toString()));
                    if (!Files.exists(distPath)) {
                        Files.createDirectories(distPath);
                    }
                } else {
                    String path = sourcePath.toAbsolutePath().toString();
                    String rep = path.replace(sourcePrefix.toString(), distPrefix.toString());
                    Path distPath = Paths.get(rep);
                    if (!Files.exists(distPath.getParent())) {
                        Files.createDirectories(distPath.getParent());
                    }
                    Files.copy(sourcePath, distPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            throw new MyException("保存失败");
        }

        if (fileShared.getFileCategory() != FileStaticKey.FILE_CATEGORY_FOLDER.toIntegerValue()) {
            FileInfo tmp = fileInfoMapper.selectByFileIdAndUserId(fileShared.getFileId(), Integer.valueOf(sharedUserId));
            FileInfo fileInfo = new FileInfo(tmp);
            String filePath = fileInfo.getFilePath();
            String fileCover = fileInfo.getFileCover();
            String newFilePath = filePath.replace(sourcePrefix.toString(), distPrefix.toString());
            String newFileCover = fileCover.replace(sourcePrefix.toString(), distPrefix.toString());
            fileInfo.setUserId(Integer.valueOf(saveUser.getUserId()));
            fileInfo.setShared(false);
            fileInfo.setFilePath(newFilePath);
            fileInfo.setFileCover(newFileCover);
            try {
                fileInfoMapper.insert(fileInfo);
            } catch (Exception e) {
                throw new MyException("文件已保存");
            }
        } else {
            FileInfo tmp = fileInfoMapper.selectByFileIdAndUserId(fileShared.getFileId(), Integer.valueOf(sharedUserId));
            FileInfo fileInfo = new FileInfo(tmp);
            String filePath = fileInfo.getFilePath();
            String newFilePath = filePath.replace(sourcePrefix.toString(), distPrefix.toString());
            fileInfo.setUserId(Integer.valueOf(saveUser.getUserId()));
            fileInfo.setShared(false);
            fileInfo.setFilePath(newFilePath);
            try {
                fileInfoMapper.insert(fileInfo);
            } catch (Exception e) {
                throw new MyException("文件已保存");
            }
        }

        List<FileShared> fileShareds = fileShareMapper.selectShareListBySharePIdAndUserId(fileShared.getShareId(), Integer.valueOf(sharedUserId));
        for (FileShared f : fileShareds) {
            if(Objects.equals(f.getFileCategory(), FileStaticKey.FILE_CATEGORY_IMAGE.toIntegerValue())) {
                FileInfo tmp = fileInfoMapper.selectByFileIdAndUserId(f.getFileId(), Integer.valueOf(sharedUserId));
                FileInfo fileInfo = new FileInfo(tmp);
                String filePath = fileInfo.getFilePath();
                String fileCover = fileInfo.getFileCover();
                String newFilePath = filePath.replace(sourcePrefix.toString(), distPrefix.toString());
                String newFileCover = fileCover.replace(sourcePrefix.toString(), distPrefix.toString());

                fileInfo.setUserId(Integer.valueOf(saveUser.getUserId()));
                fileInfo.setShared(false);
                fileInfo.setFilePath(newFilePath);
                fileInfo.setFileCover(newFileCover);
                createImageCover(saveUser.getUserId(), fileInfo.getFileName(), fileInfo);
                try {
                    fileInfoMapper.insert(fileInfo);
                } catch (Exception e) {
                    throw new MyException("文件已保存");
                }
            } else {
                FileInfo tmp = fileInfoMapper.selectByFileIdAndUserId(f.getFileId(), Integer.valueOf(sharedUserId));
                FileInfo fileInfo = new FileInfo(tmp);
                String filePath = fileInfo.getFilePath();
                String newFilePath = filePath.replace(sourcePrefix.toString(), distPrefix.toString());

                fileInfo.setUserId(Integer.valueOf(saveUser.getUserId()));
                fileInfo.setShared(false);
                fileInfo.setFilePath(newFilePath);

                try {
                    fileInfoMapper.insert(fileInfo);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("message", "保存成功");
        return ResponseEntity.ok().body(mp);
    }

    private boolean createImageCover (String userId, String fileName, FileInfo dataFileInfo) throws IOException {
        Path basePath = Paths.get(uploadDir);
        Path coverPath = basePath.resolve(userId).resolve(cover);

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
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error processing file with FFmpeg", e);
        }
        return true;
    }

}
