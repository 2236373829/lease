package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.web.app.service.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xyzZero3
 * @date 2024/8/21 18:04
 */
@SpringBootTest
class SmsServiceImplTest {

    @Autowired
    private SmsService service;

    @Test
    public void test() {
        service.sendCode("2236373829@qq.com", "123");
    }

}
