package com.order.service.impl;


import com.order.dao.OrderDao;
import com.order.model.OrderPojo;
import com.order.model.Result;
import com.order.service.OrderService;
import com.order.utils.UserContextHolder;
import com.order.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import yanson.json.Json;
import yanson.json.JsonObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/19 17:22
 */
@Service
public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao;
    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Result getOrderList(String str) {
        System.err.println(UserContextHolder.getContext().getCorrelationId());
        Result result = new Result();
        JsonObject getMap = Json.parseObject(str);
        List<Map> orderList = orderDao.selectOrderList(getMap);
        return result.setCode(200).setMsg("获取成功").setData(orderList);
    }

    @Override
    public Result createOrder(String str) {
        Result result = new Result();
        OrderPojo orderRecord = Json.parseObject(str).toJavaObject(OrderPojo.class);
        orderRecord.setOrderId(Util.nextId());
        orderRecord.setCreateTime(new Date());
        orderRecord.setStatus(1);
        Assert.isTrue(1 == orderDao.insertOrder(orderRecord), "Failed to insert order");
        return result.setCode(200).setMsg("操作成功").setData(orderRecord);
    }
}
