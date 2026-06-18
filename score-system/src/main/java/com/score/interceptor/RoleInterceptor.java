package com.score.interceptor;

import com.score.annotation.RequireRole;
import com.score.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.score.utils.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * RBAC 角色拦截器 — 所有带 @RequireRole 的方法都会经过此拦截器
 * 读注解 → 拿当前用户角色 → 对比 → 不匹配返回 403
 */
@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);
        if (requireRole == null) {
            return true;  // 没标注解，直接放行
        }

        String requiredRole = requireRole.value();
        String currentRole = SecurityUtils.getCurrentRole();

        if (currentRole == null || !currentRole.equals(requiredRole)) {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString(Result.error(403, "无权限，需要" + requiredRole + "角色")));
            out.flush();
            return false;
        }
        return true;
    }
}
