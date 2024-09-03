package com.atguigu.lease.web.admin.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xyzZero3
 * @date 2024/8/18 21:49
 */
@SpringBootTest
class ScheduleTasksTest {

    @Autowired
    private ScheduleTasks scheduleTasks;

    @Test
    public void test() {
        scheduleTasks.checkLeaseStatus();
    }

}
