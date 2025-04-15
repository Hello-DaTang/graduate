package com.wwx.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ParseUserIdFromTokenUtils {
    private static final String AUTHORIZATION_HEADER = "token"; // 请求头中 JWT 的键名
    /**
     * 从当前请求上下文中解析用户 ID
     *
     * @return 用户 ID（如果解析成功）
     */
    public Optional<Integer> getUserId() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) {
            log.warn("无法获取当前请求上下文");
            return Optional.empty();
        }

        String tokenHeader = request.getHeader(AUTHORIZATION_HEADER); // 从请求头中获取 JWT 令牌
        if (tokenHeader == null || tokenHeader.isEmpty()) {
            return Optional.empty();
        }

        try {
            // 解析 JWT 令牌
            Claims claims = JwtUtils.parseJWT(tokenHeader);
            Integer userId =(Integer) claims.get("id");
            return Optional.ofNullable(userId);
        } catch (Exception e) {
            log.warn("解析 JWT 令牌失败: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * 获取当前请求的 HttpServletRequest 对象
     *
     * @return HttpServletRequest 对象（如果存在）
     */
    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}