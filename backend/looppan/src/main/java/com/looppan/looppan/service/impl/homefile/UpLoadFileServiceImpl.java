package com.looppan.looppan.service.impl.homefile;

import com.looppan.looppan.config.globalException.MyException;
import com.looppan.looppan.config.security.UserDetailsImpl;
import com.looppan.looppan.controller.homefile.utils.FileStaticKey;
import com.looppan.looppan.mapper.FileInfoMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.User;
import com.looppan.looppan.service.homefile.UploadFileService;
import com.looppan.looppan.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UpLoadFileServiceImpl implements UploadFileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public ResponseEntity<Map> uploadFile(List<MultipartFile> files, String filePid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Integer userId = Integer.valueOf(user.getUserId());
        uploadDir += "/" + userId;



        if (filePid != null && !filePid.equals("0")) {
            filePid = filePid.substring(filePid.lastIndexOf("/") + 1);
            System.out.println("pid = " + filePid);
            FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(filePid, userId);
            System.out.println(fileInfo);
            uploadDir = fileInfo.getFilePath();
        }

        List<FileInfo> resFileinfo = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            String fileName = file.getOriginalFilename();

            List<FileInfo> curFolder = fileInfoMapper.selectByFilePidAndUserId(filePid,userId);
            System.out.println(curFolder);
            for (FileInfo fileInfo : curFolder) {
                System.out.println(fileInfo.getFileName() + "------" + fileName);
                if (Objects.equals(fileInfo.getFileName(), fileName)) {
                    throw new MyException("包含已存在的文件, 文件上传中断");
                }
            }

            String size = String.valueOf(file.getSize());
            String fileId = RandomUtils.generateRandomString(FileStaticKey.FILE_ID_LENGTH.toIntegerValue());
            LocalDateTime now = LocalDateTime.now();

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(fileId);
            fileInfo.setFileName(fileName);
            fileInfo.setFileSize(size);
            fileInfo.setCreateTime(now);
            fileInfo.setUserId(userId);
            fileInfo.setDelFlag(FileStaticKey.DEL_FLAG_NORMAL.toIntegerValue());
            fileInfo.setFilePid(filePid);
            fileInfo.setFolderType(FileStaticKey.FOLDER_TYPE_FILE.toIntegerValue());

            String contentType = file.getContentType();
            String extension = fileName != null ? fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase() : "";
            if (contentType.startsWith("image/") || extension.matches("(?i)(jpg|jpeg|png|gif)")) {
                fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_IMAGE.toIntegerValue());
            } else if (contentType.startsWith("audio/") || extension.matches("(?i)(mp3|wav|aac)")) {
                fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_AUDIO.toIntegerValue());
            } else if (contentType.startsWith("video/") || extension.matches("(?i)(mp4|avi|mov)")) {
                fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_VIDEO.toIntegerValue());
            } else if (contentType.startsWith("application/") || extension.matches("(?i)(pdf|doc|docx|xls|xlsx)")) {
                fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_DOC.toIntegerValue());
            } else {
                fileInfo.setFileCategory(FileStaticKey.FILE_CATEGORY_OTHER.toIntegerValue());
            }

            File destinationDir = new File(uploadDir);
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }
            // 创建文件对象
            File destinationFile = new File(destinationDir, file.getOriginalFilename());

            try {
                file.transferTo(destinationFile);
                System.out.println("文件已保存到: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                // 处理异常，比如文件保存失败
            }
            fileInfo.setFilePath(destinationFile.getAbsolutePath());
            fileInfoMapper.insert(fileInfo);
            resFileinfo.add(fileInfo);
        }
        Map<String, Object> mp = new HashMap<>();
        mp.put("message", "上传成功");
        mp.put("data", resFileinfo);
        return ResponseEntity.ok().body(mp);
    }
}
