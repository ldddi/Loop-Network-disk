package com.looppan.looppan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.looppan.looppan.pojo.Email;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmailMapper extends BaseMapper<Email> {

    @Update("update email_info set status = #{fail} where email = #{email} and status = #{ok}")
    public void disableEmailCode(@Param("fail") int fail, String email, @Param("ok") int ok);

    @Select("select * from email_info where email = #{email}")
    public Email getCodeByEmail(@Param("email") String email);
}
