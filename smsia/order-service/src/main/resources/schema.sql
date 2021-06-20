CREATE TABLE `ms_order` (
    `order_id` bigint(20) NOT NULL COMMENT '订单ID',
    `status` int(11) NOT NULL COMMENT '状态',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8
