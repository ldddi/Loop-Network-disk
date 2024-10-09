package com.looppan.looppan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.looppan.looppan.pojo.FileInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {
    // 根据用户id和文件id查找当前文件
    @Select("select * from file_info where file_id = #{fileId} and user_id = #{userId}")
    FileInfo selectByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") Integer userId);

    // 根据userid和 文件的pid，查找当前文件夹下的所有文件
    @Select("SELECT * FROM file_info WHERE user_id = #{userId} AND (file_pid IS NULL OR file_pid = #{filePid})")
    List<FileInfo> selectByUserId(@Param("userId") Integer userId, @Param("filePid") String filePid);

    // 根据用户id和查找的种类，查找不同类型的文件
    @Select("select * from file_info where user_id = #{userId} and file_category = #{category}")
    List<FileInfo> selectByUserIdAndCategory(@Param("userId") Integer userId, @Param("category") Integer category);

    // 根据userid和 文件的pid，查找当前文件夹下的所有文件
    @Select("select * from file_info where file_pid = #{filePId} and user_id = #{userId}")
    List<FileInfo> selectByFilePidAndUserId(@Param("filePId") String filePId, @Param("userId") Integer userId);

    // 根据userid和 pid 查找所有文件夹或文件
    @Select("select * from file_info where file_pid = #{filePId} and user_id = #{userId}")
    List<FileInfo> selectByFilePidAndUserIdAndLimitType(@Param("filePId") String filePId,
                                                       @Param("userId") Integer userId,
                                                       @Param("folderType") Integer folderType);
    // 根据用户id和文件id删除当前文件
    @Delete("delete from file_info where file_id = #{fileId} and user_id = #{userId}")
    Integer DeleteByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") Integer userId);

}
