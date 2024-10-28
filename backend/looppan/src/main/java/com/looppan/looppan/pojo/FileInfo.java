package com.looppan.looppan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_info")
public class FileInfo {
    @TableId(value = "file_id", type = IdType.NONE) // 标记为主键
    String fileId;

//    @TableField(value = "user_id") // 使用 @TableField 来标记第二个主键字段
    Integer userId;

    String fileLastPath;
    String filePid;
    String fileSize;
    String fileName;
    String fileCover;
    String filePath;
    LocalDateTime createTime;
    LocalDateTime lastUpdateTime;
    Integer folderType;
    Integer fileCategory;
    LocalDateTime recoveryTime;
    Integer delFlag;
    Boolean shared;

    public FileInfo(FileInfo tmp) {
        this.fileId = tmp.getFileId();
        this.userId = tmp.getUserId();
        this.fileLastPath = tmp.getFileLastPath();
        this.filePid = tmp.getFilePid();
        this.fileSize = tmp.getFileSize();
        this.fileName = tmp.getFileName();
        this.fileCover = tmp.getFileCover();
        this.filePath = tmp.getFilePath();
        this.createTime = tmp.getCreateTime();
        this.lastUpdateTime = tmp.getLastUpdateTime();
        this.folderType = tmp.getFolderType();
        this.fileCategory = tmp.getFileCategory();
        this.recoveryTime = tmp.getRecoveryTime();
        this.delFlag = tmp.getDelFlag();
        this.shared = tmp.getShared();
    }
}
