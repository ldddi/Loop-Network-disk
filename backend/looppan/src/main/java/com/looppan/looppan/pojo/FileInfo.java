package com.looppan.looppan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_info")
public class FileInfo {
    @TableId
    String fileId;

    Integer userId;
    String fileMd5;
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
}
