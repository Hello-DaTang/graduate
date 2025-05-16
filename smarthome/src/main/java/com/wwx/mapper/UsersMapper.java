package com.wwx.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.wwx.pojo.Users;

@Mapper
public interface UsersMapper {

    public void insert(Users users);

    @Select("select * from users where username=#{username} and password=#{password}")
    public Users getUserByUsername(Users users);

    public void update(Users users);

    public void delete(Integer id);

    @Select("select * from users where id=#{userId}")
    public Users getById(Integer userId);
    @Select("select * from users where username=#{username}")
    public Users findByUsername(String username);

}
