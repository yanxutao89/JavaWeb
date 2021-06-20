package com.order.controller;


import com.order.model.Result;
import com.order.service.OrderService;
import com.order.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
public class OrderController {

    private OrderService orderService;
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/retrieve/orderList", method = RequestMethod.GET)
    public Result getOrganization() {
        return orderService.getOrderList("");
    }

}
