package com.wwx.aop;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.wwx.mapper.OperateLogMapper;
import com.wwx.pojo.OperateLog;
// import com.wwx.utils.JwtUtils;
import com.wwx.utils.ParseUserIdFromTokenUtils;

// import io.jsonwebtoken.Claims;
// import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAspect {

    // @Autowired
    // private HttpServletRequest request;

    @Autowired
    private ParseUserIdFromTokenUtils parseUserIdFromTokenUtils;

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.wwx.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // //操作人ID-当前登录员TID
        // //获取请求头中的jwt令牌,解析令牌
        // String jwt = request.getHeader("token");
        // Claims claims = JwtUtils.parseJWT(jwt);
        Integer operateUser = parseUserIdFromTokenUtils.getUserId().orElseThrow(); // 如果解析失败则抛出异常

        //操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        //操作类名
        String classNmae = joinPoint.getTarget().getClass().getName();

        //操作方法名
        String methodName = joinPoint.getSignature().getName();

        //操作方法参数
        Object[] args = joinPoint.getArgs();
        String methodParam = Arrays.toString(args);

        //调用原始方法
        Long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        Long end = System.currentTimeMillis();

        //方法返回值
        String returnValue = JSONObject.toJSONString(result);

        //操作耗时
        Long costTime = end - start;

       

        //记录日志
        OperateLog operateLog = new OperateLog(null, operateUser, operateTime, classNmae, methodName, methodParam, returnValue, costTime);
        operateLogMapper.insert(operateLog);

        log.info("AOP记录日志:{}", operateLog);
        return result;
    }
}
