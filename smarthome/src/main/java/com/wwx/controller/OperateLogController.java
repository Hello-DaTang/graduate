package com.wwx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.wwx.anno.Log;
import com.wwx.pojo.OperateLog;
import com.wwx.pojo.PageBean;
import com.wwx.pojo.Result;
import com.wwx.service.OperateLogService;
import com.wwx.utils.ParseUserIdFromTokenUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RestController
@RequestMapping("/operateLog")
public class OperateLogController {
    @Autowired
    OperateLogService operateLogService;
    @Autowired
    private ParseUserIdFromTokenUtils parseUserIdFromTokenUtils;

    //获取当前用户操作信息
    @RequestMapping("/info")
    public Result get() {
        log.info("获取用户操作信息");
        Integer userId = parseUserIdFromTokenUtils.getUserId().orElseThrow(); // 如果解析失败则抛出异常

        PageBean pageBean = operateLogService.getByOperaterUserId(userId);
        return Result.success(pageBean);
    }

    
}
