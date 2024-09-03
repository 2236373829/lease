package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.web.app.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    JavaMailSender mailSender;

    @Override
    public void sendCode(String phone, String code) {
        // 创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2236373829@qq.com");
        message.setTo(phone);
        message.setSubject("验证码");
        message.setText("验证码:" + code);
        mailSender.send(message);
    }
}
