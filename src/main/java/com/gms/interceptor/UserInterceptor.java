package com.gms.interceptor;

import com.gms.entity.User;
import com.gms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/7/13 14:25
 */
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        User user = (User) redisTemplate.opsForValue().get(token);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未登录");
            return false;
        }

        request.getSession().setAttribute("currentUser", user);
        return true;
    }
}
