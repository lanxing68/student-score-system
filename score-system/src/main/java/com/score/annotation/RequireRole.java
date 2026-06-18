package com.score.annotation;

import java.lang.annotation.*;

/**
 * RBAC 角色权限注解 — 标注在 Controller 方法上
 * 例：@RequireRole("TEACHER") — 只有教师能调此接口
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    String value();  // "STUDENT" 或 "TEACHER"
}
