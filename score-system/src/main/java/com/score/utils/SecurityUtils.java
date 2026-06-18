package com.score.utils;

/**
 * 安全工具类 — 从 ThreadLocal 获取当前登录用户信息
 * 由队友 A 的 JwtFilter 在每次请求时填充
 * 组长（我）在 RoleInterceptor 和 Controller 中调用
 */
public class SecurityUtils {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ROLE = new ThreadLocal<>();

    public static void setCurrentUser(Long userId, String role) {
        USER_ID.set(userId);
        USER_ROLE.set(role);
    }

    public static Long getCurrentUserId() {
        return USER_ID.get();
    }

    public static String getCurrentRole() {
        return USER_ROLE.get();
    }

    /** 请求结束后清理，防止内存泄漏 */
    public static void clear() {
        USER_ID.remove();
        USER_ROLE.remove();
    }
}
