package com.jeremy.seckill.db.dao;

import com.jeremy.seckill.db.po.Order;

public interface OrderDao {

    void insertOrder(Order order);

    Order queryOrder(String orderNo);

    void updateOrder(Order order);
}
