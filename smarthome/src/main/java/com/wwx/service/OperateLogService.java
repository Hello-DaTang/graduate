package com.wwx.service;

import com.wwx.pojo.OperateLog;
import com.wwx.pojo.PageBean;

public interface OperateLogService {
    /**
     * 根据操作用户ID获取操作日志
     * @param userId 用户ID
     * @return 操作日志对象
     */
    PageBean getByOperaterUserId(Integer userId);
}
