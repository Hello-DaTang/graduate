package com.wwx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.wwx.anno.Log;
import com.wwx.pojo.Result;
import com.wwx.pojo.Users;
import com.wwx.service.UsersService;
import com.wwx.utils.ParseUserIdFromTokenUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UsersService usersService;
    @Autowired
    private ParseUserIdFromTokenUtils parseUserIdFromTokenUtils;

    //获取当前用户信息
    @RequestMapping("/info")
    public Result get() {
        log.info("获取用户信息");
        //获取当前用户id
        Integer userId = parseUserIdFromTokenUtils.getUserId().orElseThrow(); // 如果解析失败则抛出异常

        //调用service获取用户信息
        Users users = usersService.getById(userId);

        return Result.success(users);
    }

    //更新用户信息
    @Log
    @PutMapping("/update")
    public Result update(@RequestBody Users users) {
        log.info("更新用户信息：{}", users);
        users.setId(parseUserIdFromTokenUtils.getUserId().orElseThrow()); // 如果解析失败则抛出异常
        //调用service更新用户信息
        usersService.update(users);
        return Result.success();
    }

    //删除用户信息
    @Log
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除用户信息：{}", id);
        //调用service删除用户信息
        usersService.delete(id);
        return Result.success();
    }
}