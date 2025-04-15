package com.wwx.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wwx.pojo.Result;
import com.wwx.pojo.Users;
import com.wwx.service.UsersService;
import com.wwx.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/login")
    public Result login(@RequestBody Users users){
        log.info("登录：{}",users);
        Users e = usersService.login(users);
        //登录成功、生成jwt、返回jwt
        if(e!=null){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("name", e.getName());
            claims.put("username", e.getUsername());
            String jwt = JwtUtils.generateJwt(claims);//jwt包含用户信息
            return Result.success(jwt);
        }
        //登录失败  
        return Result.error("用户名或密码错误");
    }
}
