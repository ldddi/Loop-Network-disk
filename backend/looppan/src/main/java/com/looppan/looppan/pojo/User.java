package com.looppan.looppan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private String userId;
    private String nickName;
    private String email;
    private String avatar;
    private String password;
    private LocalDateTime joinTime;
    private LocalDateTime lastLoginTime;
    private BigInteger totalSpace;
    private BigInteger useSpace;
}
