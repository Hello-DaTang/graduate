package com.wwx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wwx.mapper.OperateLogMapper;
import com.wwx.pojo.OperateLog;
import com.wwx.pojo.PageBean;
import com.wwx.service.OperateLogService;

@Service
public class OperateLogServiceImpl implements OperateLogService {
    
    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageBean getByOperaterUserId(Integer userId) {
        // 1. 设置分页参数
        PageHelper.startPage(1, 5);
        // 2. 执行查询 - 修正
        List<OperateLog> list = operateLogMapper.getByOperaterUserId(userId);
        // 3. 获取分页信息
        Page<OperateLog> page = (Page<OperateLog>) list;
        // 4. 封装 PageBean 对象
        return new PageBean(page.getTotal(), page.getResult());
    }
}
