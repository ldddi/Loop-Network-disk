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
@TableName("email_info")
public class Email {
    @TableId(value = "email", type = IdType.ASSIGN_UUID)
    private String email;
    private String code;
    private LocalDateTime createTime;
    private Integer status;
}
