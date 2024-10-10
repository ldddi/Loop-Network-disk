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

        List<FileInfo> resFileinfo = new ArrayList<>();
        if (filePid != null && !filePid.equals("0")) {
            filePid = filePid.substring(filePid.lastIndexOf("/") + 1);
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            String fileName = file.getOriginalFilename();

            String size = String.valueOf(file.getSize());
            String fileId = RandomUtils.generateRandomString(FileStaticKey.FILE_ID_LENGTH.toIntegerValue());
            LocalDateTime now = LocalDateTime.now();

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(fileId);
            fileInfo.setFileName(fileName);
            fileInfo.setFileSize(size);
            fileInfo.setCreateTime(now);
            fileInfo.setLastUpdateTime(now);
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

            // 创建文件对象
            File destinationFile = null;
            if (filePid == null || filePid.equals("0")){
                destinationFile = new File(uploadDir + "/" + userId, fileName);
                if (destinationFile.exists()) {
                    String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + fileId + "." + extension;
                    destinationFile = new File(uploadDir + "/" + userId, newFileName);
                    fileInfo.setFileName(newFileName);
                }
            } else {
                FileInfo pFileInfo = fileInfoMapper.selectByFileIdAndUserId(filePid, userId);
                destinationFile = new File(pFileInfo.getFilePath() + "/", fileName);
                if (destinationFile.exists()) {
                    String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + fileId + "." + extension;
                    destinationFile = new File(uploadDir + "/" + userId, newFileName);
                    fileInfo.setFileName(newFileName);
                }
            }

            fileInfo.setFilePath(destinationFile.getAbsolutePath());

            try {
                fileInfoMapper.insert(fileInfo);
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException("文件存入数据库失败");
            }
            try {
                file.transferTo(destinationFile);
            } catch (IOException e) {
                throw new MyException("存储文件失败!");
            }
            resFileinfo.add(fileInfo);
        }
        Map<String, Object> mp = new HashMap<>();
        mp.put("message", "上传成功");
        mp.put("data", resFileinfo);
        return ResponseEntity.ok().body(mp);
    }
}
