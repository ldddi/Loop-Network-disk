package com.looppan.looppan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.looppan.looppan.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user_info where email = #{email}")
    User selectByEmail(String email);

    @Select("select * from user_info where nick_name = #{nick_name}")
    User selectByNickName(String nick_name);
}
