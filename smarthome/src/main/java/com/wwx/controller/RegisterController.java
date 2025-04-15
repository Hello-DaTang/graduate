package com.wwx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wwx.pojo.Result;
import com.wwx.pojo.Users;
import com.wwx.service.UsersService;

@RestController
public class RegisterController {
    @Autowired
    private UsersService usersService;
    @PostMapping("/register")
    public Result register(@RequestBody Users users) {//需要有username、password、name、entrydate四个参数JSON格式传入
        usersService.register(users);
        return Result.success("注册成功");
    }
}
