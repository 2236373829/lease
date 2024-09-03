package com.atguigu.lease.common.login;

/**
 * 使用线程保存用户登录信息
 * @author xyzZero3
 * @date 2024/8/20 19:20
 */

public class LoginUserHolder {

    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        threadLocal.set(loginUser);
    }

    public static LoginUser get() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }

}
