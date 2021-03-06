package com.order;

import com.order.datasource.EnableDynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@MapperScan("com.order.dao")
@EnableDynamicDataSource
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.order"})
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}

