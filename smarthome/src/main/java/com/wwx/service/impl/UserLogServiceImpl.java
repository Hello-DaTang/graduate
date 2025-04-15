package com.wwx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wwx.mapper.UserLogMapper;
import com.wwx.pojo.UserLog;
import com.wwx.service.UserLogService;

@Service
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    private UserLogMapper userLogMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void insert(UserLog userLog) {
        userLogMapper.insert(userLog);
    }

}
