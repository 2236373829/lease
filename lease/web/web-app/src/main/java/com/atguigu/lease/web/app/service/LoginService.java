package com.atguigu.lease.web.app.service;

import com.atguigu.lease.web.app.vo.user.LoginVo;
import com.atguigu.lease.web.app.vo.user.UserInfoVo;

public interface LoginService {
    /**
     * 发送验证码
     * @param phone
     */
    void sendCode(String phone);

    /**
     * 登录或注册
     * @param loginVo
     * @return
     */
    String login(LoginVo loginVo);

    UserInfoVo getLoginUserById(Long userId);
}
