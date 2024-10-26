package com.looppan.looppan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.looppan.looppan.pojo.FileInfo;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {
//    select

    /**
     * 根据用户id和文件id查找当前文件
     * @param fileId
     * @param userId
     * @return
     */
    @Select("select * from file_info where file_id = #{fileId} and user_id = #{userId}")
    FileInfo selectByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") Integer userId);

    /**
     * 根据用户id和查找的种类，查找不同类型的文件
     * @param userId
     * @param category
     * @return
     */
    @Select("select * from file_info where user_id = #{userId} and file_category = #{category}  and del_flag = 2 order by create_time desc")
    List<FileInfo> selectByUserIdAndCategory(@Param("userId") Integer userId, @Param("category") Integer category);

    /**
     * 根据用户id和 pid，查找 所有文件和文件夹
     * @param filePId
     * @param userId
     * @return
     */
    @Select("select * from file_info where file_pid = #{filePId} and user_id = #{userId} and del_flag = 2  ORDER BY create_time DESC")
    List<FileInfo> selectByFilePidAndUserId(@Param("filePId") String filePId, @Param("userId") Integer userId);

    /**
     * 根据用户id和 pid，查找 所有文件和文件夹 的filename
     * @param filePId
     * @param userId
     * @return
     */
    @Select("select file_name from file_info where file_pid = #{filePId} and user_id = #{userId} and del_flag = 2")
    List<String> selectFilenameByFilePidAndUserId(@Param("filePId") String filePId, @Param("userId") Integer userId);


    /**
     * 根据用户id和 pid 查找 所有文件夹或文件
     * @param filePId
     * @param userId
     * @param folderType
     * @return
     */
    @Select("select * from file_info where file_pid = #{filePId} and user_id = #{userId} and folder_type = #{folderType} and del_flag = 2")
    List<FileInfo> selectByFilePidAndUserIdAndLimitType(@Param("filePId") String filePId,
                                                       @Param("userId") Integer userId,
                                                       @Param("folderType") Integer folderType);

    /**
     * 根据用户id 和 del_flag 查找在回收站的文件和文件夹
     * @param userId
     * @return
     */
    @Select("select * from file_info where user_id = #{userId} and del_flag = 1")
    List<FileInfo> selectRecycleFilesByUserId(@Param("userId") Integer userId, @Param("delFlag") Integer delFlag);
//    delete

    /**
     * 根据用户id和文件id 彻底删除当前文件
     * @param fileId
     * @param userId
     * @return
     */
    @Delete("delete from file_info where file_id = #{fileId} and user_id = #{userId}")
    Integer DeleteByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") Integer userId);

//    update

    /**
     * 根据用户id和文件id 将当前文件放入回收站
     * @param fileId
     * @param userId
     * @param delFlag
     * @param now
     * @return
     */
    @Update("update file_info set del_flag = #{delFlag}, recovery_time = #{now} where file_id = #{fileId} and user_id = #{userId}")
    Integer updateDelFlagByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") Integer userId, @Param("delFlag") Integer delFlag, @Param("now")LocalDateTime now);

    /**
     * 根据 fileId和userId 更新 filePid和filePath
     * @param fileId
     * @param userId
     * @param filePId
     * @param filePath
     * @return
     */
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

    @Update("UPDATE file_info SET shared = #{shared} WHERE file_id = #{fileId} AND user_id = #{userId}")
    Integer updateSharedByFileIdAndUserId(@Param("fileId") String fileId,
                                                   @Param("userId") String userId,
                                                   @Param("shared") boolean shared);

//    insert
    @Insert("INSERT INTO file_info (file_id, user_id, file_md5, file_pid, file_size, file_name, " +
            "file_cover, file_path, create_time, last_update_time, folder_type, file_category, " +
            "recovery_time, del_flag, shared) " +
            "VALUES (#{fileId}, #{userId}, #{fileMd5}, #{filePid}, #{fileSize}, #{fileName}, " +
            "#{fileCover}, #{filePath}, #{createTime}, #{lastUpdateTime}, #{folderType}, " +
            "#{fileCategory}, #{recoveryTime}, #{delFlag}, #{shared})")
    int insertFileInfo(FileInfo fileInfo);
}
