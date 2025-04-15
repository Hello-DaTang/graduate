package com.wwx.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.wwx.pojo.UserLog;

@Mapper
public interface UserLogMapper {

    @Insert("insert into user_log(id,description,create_time) values(#{id},#{description},#{createTime})")
    void insert(UserLog userLog);

}
