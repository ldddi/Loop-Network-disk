package com.looppan.looppan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.looppan.looppan.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user_info where email = #{email}")
    User selectByEmail(String email);

    @Select("select * from user_info where nick_name = #{nick_name}")
    User selectByNickName(String nick_name);

    @Select("SELECT * FROM user_info WHERE user_id = #{userId} FOR UPDATE")
    User selectByIdForUpdate(String userId);

    @Select("SELECT count(*) from user_info")
    int selectCount();

    @Select("select * from user_info ORDER BY user_id asc limit 2")
    List<User> selectLast();

}
