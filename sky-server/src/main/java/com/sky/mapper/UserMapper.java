package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.User;

@Mapper
public interface UserMapper {

    /**
     * 通过openid获取用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 将新用户插入数据库
     * @param user
     */
    void insert(User user);

    /**
     * 根据id查询用户
     * @param id
     */
    @Select("select * from user where id = #{id}")
    User getById(Long id);

    /**
     * 根据邮箱查询用户
     * @param email
     * @return
     */
    @Select("select * from user where email = #{email}")
    User getByEmail(String email);
}
