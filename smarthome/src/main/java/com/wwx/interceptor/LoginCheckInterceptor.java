package com.wwx.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson2.JSONObject;
import com.wwx.pojo.Result;
import com.wwx.utils.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor{

    //目标资源执行之前执行即controller方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler)
            throws Exception {
        // TODO Auto-generated method stub
        //1.获取请求url.
        String url = req.getRequestURL().toString();
        log.info("请求url:{}",url);

        //2.判断请求url中是否包含login，如果包含，说明是登录操作、放行。
        if(url.contains("login")||url.contains("register")){
            log.info("放行");
            return true;
        }

        //3.获取请求头中的令牌(token)。
        String jwt = req.getHeader("token");

        //4.判断令牌是否存在，如果不存在，返回错误结果(未登录)。
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头token为空未登录");
            Result error = Result.error("NOT_LOGIN");
            //手动转换 对象转json
            String notLogin =JSONObject.toJSONString(error);

            resp.getWriter().write(notLogin);
            return false;
        }
        //5.解析token，如果解析失败就报错转进catch，返回错误结果(未登录)
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e){
            e.printStackTrace();
            log.info("解析token失败，未登录");
            Result error = Result.error("NOT_LOGIN");
            //手动转换 对象转json------------------->阿里巴巴快速转json
            String notLogin =JSONObject.toJSONString(error);

            resp.getWriter().write(notLogin);
            return false;
        }

        //6.放行。

        log.info("放行");
        return true;
        // return HandlerInterceptor.super.preHandle(req, resp, handler);
    }
    
}