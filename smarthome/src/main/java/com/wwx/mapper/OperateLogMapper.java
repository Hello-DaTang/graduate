package com.wwx.mapper;

import com.wwx.pojo.OperateLog;
import com.wwx.pojo.SmartHome;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OperateLogMapper {

    //插入日志数据
    @Insert("insert into operate_log (operate_user, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{operateUser}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime});")
    public void insert(OperateLog log);

    /**
     * 根据操作用户ID获取操作日志
     * @param userId 用户ID
     * @return 操作日志对象
     */
    @Select("select * from operate_log where operate_user=#{userId}")
    List<OperateLog> getByOperaterUserId(@Param("userId") Integer userId);
}
