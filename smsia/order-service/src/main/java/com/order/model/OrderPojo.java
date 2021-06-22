package com.order.model;

import java.util.Date;

public class OrderPojo {
    private Long orderId;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    public Long getOrderId() {
        return orderId;
    }

    public OrderPojo setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public OrderPojo setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public OrderPojo setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public OrderPojo setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "OrderPojo{" +
                "orderId=" + orderId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
