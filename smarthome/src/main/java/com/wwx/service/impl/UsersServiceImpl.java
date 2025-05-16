package com.wwx.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwx.mapper.HomeMapper;
import com.wwx.mapper.UsersMapper;
import com.wwx.pojo.UserLog;
import com.wwx.pojo.Users;
import com.wwx.service.UserLogService;
import com.wwx.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {

    //注入mapper
    @Autowired
    UsersMapper usersMapper;

    @Autowired
    HomeMapper homeMapper;

    @Autowired
    UserLogService userLogService;

    @Override
    public void register(Users users) {
        //获得当前时间戳
        users.setCreateTime(LocalDateTime.now());
        users.setUpdateTime(LocalDateTime.now());
        usersMapper.insert(users);
    }

    @Override
    public Users login(Users users) {
        return usersMapper.getUserByUsername(users);
    }

    @Override
    public void update(Users users) {
        //获得当前时间戳
        try{
            users.setUpdateTime(LocalDateTime.now());
            usersMapper.update(users);
        } finally {
            UserLog userLog = new UserLog();
            userLog.setCreateTime(LocalDateTime.now());
            userLog.setDescription("修改用户:"+users.getId());
            userLogService.insert(userLog);
        }
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public void delete(Integer id) {
        try{
            usersMapper.delete(id);
            homeMapper.deleteByUserId(id);
        } finally {
            UserLog userLog = new UserLog();
            userLog.setCreateTime(LocalDateTime.now());
            userLog.setDescription("删除用户:"+id);
            userLogService.insert(userLog);

        }
    }

    @Override
    public Users getById(Integer userId) {
        return usersMapper.getById(userId);
    }

    @Override
    public Users findByUsername(String username) {
        return usersMapper.findByUsername(username);
    }

}
