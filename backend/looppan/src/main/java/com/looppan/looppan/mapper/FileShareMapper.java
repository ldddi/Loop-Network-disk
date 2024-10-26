package com.looppan.looppan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.looppan.looppan.pojo.FileInfo;
import com.looppan.looppan.pojo.FileShared;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileShareMapper extends BaseMapper<FileShared> {

    @Select("select * from file_shared where user_id = #{userId} and share_time IS NOT NULL")
    List<FileShared> selectByUserId(@Param("userId") Integer userId);

    @Select("select * from file_shared where share_p_id = #{sharePId} and user_id = #{userId}")
    List<FileShared> selectShareListBySharePIdAndUserId(@Param("sharePId") String sharePId,
                                                        @Param("userId") Integer userId);

//    @Delete("delete from file_shared where share_id = #{shareId};")
//    int deleteByShareId(@Param("shareId") Integer shareId);
}
