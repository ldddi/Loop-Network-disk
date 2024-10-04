package com.looppan.looppan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.looppan.looppan.pojo.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {
    @Select("select * from file_info where file_id = #{fileId} and user_id = #{userId}")
    FileInfo selectByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") Integer userId);
}
