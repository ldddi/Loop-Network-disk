package com.looppan.looppan.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_shared")
public class FileShared {
    @TableId(value = "share_id")
    String shareId;
    String fileId;
    Integer userId;
    String userNickName;
    String userAvatar;
    String fileName;
    String filePath;
    String fileSize;
    String fileUrl;
    Integer fileCategory;
    LocalDateTime shareTime;
    LocalDateTime failTime;
    String extractionCode;
    Integer views;
}
