package com.atguigu.lease.common.utils;

import java.util.Random;

/**
 * 获取随机给定长度的验证码
 * @author xyzZero3
 * @date 2024/8/21 18:11
 */
public class CodeUtil {

    public static String getRandomCode(Integer length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(10);
            stringBuilder.append(num);
        }
        return stringBuilder.toString();
    }

}
