package com.looppan.looppan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.looppan.looppan.pojo.FileInfo;
import org.apache.ibatis.annotations.*;

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

    /**
     * 根据userid和 文件的pid，查找当前文件夹下的所有文件
     * @param filePId
     * @param userId
     * @return
     */
    @Select("select * from file_info where file_pid = #{filePId} and user_id = #{userId}  ORDER BY create_time DESC")
    List<FileInfo> selectByFilePidAndUserId(@Param("filePId") String filePId, @Param("userId") Integer userId);

    // 根据userid和 pid 查找所有文件夹或文件
    @Select("select * from file_info where file_pid = #{filePId} and user_id = #{userId} and folder_type = #{folderType}")
    List<FileInfo> selectByFilePidAndUserIdAndLimitType(@Param("filePId") String filePId,
                                                       @Param("userId") Integer userId,
                                                       @Param("folderType") Integer folderType);
    // 根据userid 查找当前文件夹下的所有文件
    @Select("select * from file_info where user_id = #{userId} and file_category = #{type}")
    List<FileInfo> selectAllFilesByUserId(@Param("userId") Integer userId, @Param("type") Integer category);

    // 根据用户id和文件id删除当前文件
    @Delete("delete from file_info where file_id = #{fileId} and user_id = #{userId}")
    Integer DeleteByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") Integer userId);

    @Update("UPDATE file_info SET file_pid = #{filePId}, file_path = #{filePath}  WHERE file_id = #{fileId} AND user_id = #{userId}")
    Integer updateByFileIdAndUserId(@Param("fileId") String fileId,
                                    @Param("userId") String userId,
                                    @Param("filePId") String filePId,
                                    @Param("filePath") String filePath);

    /**
     * 根据文件id和用户id修改文件名
     * @param fileId
     * @param userId
     * @param newName
     * @return
     */
    @Update("UPDATE file_info SET file_name = #{newName}, file_path = #{newPath} WHERE file_id = #{fileId} AND user_id = #{userId}")
    Integer updateFileNameAndPathByFileIdAndUserId(@Param("fileId") String fileId,
                                    @Param("userId") String userId,
                                    @Param("newName") String newName,
                                                   @Param("newPath") String newPath);

}
