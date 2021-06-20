package com.order.service.impl;


import com.order.dao.OrderDao;
import com.order.model.Result;
import com.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Result result = new Result();
        List<Map> orderList = orderDao.selectOrderList(new HashMap());
        return result.setCode(200).setMsg("获取成功").setData(orderList);
    }
}
