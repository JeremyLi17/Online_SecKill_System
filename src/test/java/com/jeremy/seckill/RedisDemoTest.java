package com.jeremy.seckill;

import com.jeremy.seckill.services.SeckillActivityService;
import com.jeremy.seckill.util.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
public class RedisDemoTest {
    @Resource
    private RedisService redisService;

    @Test
    public void stockTest() {
        redisService.setValue("stock:19", 10L);
    }

    @Test
    public void getStockTest() {
        String stock = redisService.getValue("stock:19");
        System.out.println(stock);
    }

    @Test
    public void stockDeductValidatorTest() {
        boolean result = redisService.stockDeductValidator("stock:19");
        System.out.println("result:" + result);
        String stock = redisService.getValue("stock:19");
        System.out.println("stock:" + stock);
    }

    @Autowired
    SeckillActivityService seckillActivityService;

    @Test
    public void pushSeckillInfoToRedisTest() {
        seckillActivityService.pushSeckillInfoToRedis(19);
    }

    @Test
    public void testConcurrentAddLock() {
        for (int i = 0; i < 10; i++) {
            String requestId = UUID.randomUUID().toString();
            System.out.println(redisService.tryGetDistributedLock("A", requestId, 1000));
            redisService.releaseDistributedLock("A",requestId);
        }
    }
}
