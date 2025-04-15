package com.wwx.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wwx.pojo.SmartHome;

@Mapper
public interface HomeMapper {

    List<SmartHome> list(String homeName, Integer userId, LocalDate begin, LocalDate end);

    void delete(List<Integer> ids);

    void insert(SmartHome smartHome);

    SmartHome getById(Integer id);

    void update(SmartHome smartHome);

    void deleteByUserId(Integer userId); // 删除用户时删除用户的家居信息

}
