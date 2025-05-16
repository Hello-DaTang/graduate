package com.wwx.service;


import com.wwx.pojo.Users;

public interface UsersService {

    //注册用户
    public void register(Users users);

    public Users login(Users users);

    public void update(Users users);

    public void delete(Integer id);

    public Users getById(Integer userId);

    public Users findByUsername(String username);
}
