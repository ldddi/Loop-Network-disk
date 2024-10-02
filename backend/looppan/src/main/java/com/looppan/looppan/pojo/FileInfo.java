package com.looppan.looppan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_info")
public class FileInfo {
    @TableId(type = IdType.ASSIGN_ID)
    Integer fileId;
    Integer userId;
    String fileMd5;
    String filePid;
    String fileSize;
    String fileName;
    String fileCover;
    LocalDateTime createTime;
    LocalDateTime lastUpdateTime;
    Integer folderType;
    Integer fileCategory;
    Integer status;
    LocalDateTime recoveryTime;
    Integer delFlag;
}
