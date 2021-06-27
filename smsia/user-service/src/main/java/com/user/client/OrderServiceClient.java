package com.user.client;

import com.user.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("order-service")
public interface OrderServiceClient {
    @PostMapping(value = "/v1/order/retrieve/list")
    Result getOrderService(@RequestBody String str);
}
