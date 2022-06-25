package com.jeremy.seckill.services;

import com.alibaba.fastjson.JSON;
import com.jeremy.seckill.db.dao.OrderDao;
import com.jeremy.seckill.db.dao.SeckillActivityDao;
import com.jeremy.seckill.db.po.Order;
import com.jeremy.seckill.db.po.SeckillActivity;
import com.jeremy.seckill.services.mq.RocketMQService;
import com.jeremy.seckill.util.RedisService;
import com.jeremy.seckill.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class SeckillActivityService {
    @Autowired
    private RedisService redisService;

    @Resource
    private SeckillActivityDao seckillActivityDao;

    @Resource
    private RocketMQService rocketMQService;

    private SnowFlake snowFlake = new SnowFlake(1,1);

    /**
     * 判断商品是否还有库存
     * @param activityId
     * @return
     */
    public boolean seckillStockValidator(long activityId) {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }

    public Order createOrder(long seckillActivityId, long userId) throws Exception {
        // 创建订单
        SeckillActivity activity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        Order order = new Order();
        // 采用雪花算法生成订单ID
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setSeckillActivityId(activity.getId());
        order.setUserId(userId);
        order.setOrderAmount(activity.getSeckillPrice().longValue());
        // 发送创建订单消息
        rocketMQService.sendMessage("seckill_order", JSON.toJSONString(order));
        return order;
    }

    @Resource
    OrderDao orderDao;

    /**
     * 订单支付完成处理
     * @param orderNo
     */
    public void payOrderProcess(String orderNo) {
        log.info("完成支付订单，订单号：" + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        boolean deductStockResult = seckillActivityDao.deductStock(order.getSeckillActivityId());
        if (deductStockResult) {
            order.setPayTime(new Date());
            // 订单状态（2）：完成支付
            order.setOrderStatus(2);
            orderDao.updateOrder(order);
        }
    }
}
