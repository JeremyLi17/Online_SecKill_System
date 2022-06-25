package com.jeremy.seckill;

import com.jeremy.seckill.services.mq.RocketMQService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class MQTest {
    @Autowired
    RocketMQService rocketMQService;

    @Test
    public void sendText() throws Exception {
        rocketMQService.sendMessage("test-jeremy","Hello World!" + new Date().toString());
    }
}
