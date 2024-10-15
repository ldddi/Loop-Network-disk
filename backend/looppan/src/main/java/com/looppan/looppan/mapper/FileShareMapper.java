package com.looppan.looppan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.looppan.looppan.pojo.FileShared;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileShareMapper extends BaseMapper<FileShared> {

    @Select("select * from file_shared where user_id = #{userId}")
    List<FileShared> selectByUserId(@Param("userId") Integer userId);
}
