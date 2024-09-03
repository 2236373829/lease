package com.atguigu.lease.web.admin.custom.interceptor;

import com.atguigu.lease.common.login.LoginUser;
import com.atguigu.lease.common.login.LoginUserHolder;
import com.atguigu.lease.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * token认证拦截器
 * @author xyzZero3
 * @date 2024/8/20 18:33
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String accessToken = request.getHeader("access-token");
        Claims claims = JwtUtil.parseToken(accessToken);
        Long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);

        // 每次请求之前从请求头中获取token信息放进线程中
        LoginUserHolder.setLoginUser(new LoginUser(userId, username));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 单次请求结束后清理线程中的token信息 方便线程池回收线程
        LoginUserHolder.clear();
    }
}
