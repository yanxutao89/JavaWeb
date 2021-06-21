CREATE TABLE `ms_user` (
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名称',
    `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户密码',
    `status` int(2) DEFAULT NULL COMMENT '状态,0-删除,1-正常',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC

CREATE TABLE `ms_logger` (
     `logger_id` bigint(20) NOT NULL COMMENT '日志ID',
     `request_no` bigint(20) DEFAULT NULL COMMENT '请求编号',
     `request_md5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '请求md5',
     `request_time` datetime DEFAULT NULL COMMENT '请求时间',
     `request_params` longtext COMMENT '请求参数',
     `response_time` datetime DEFAULT NULL COMMENT '响应时间',
     `response_params` longtext COMMENT '响应参数',
     `rtt` bigint(20) DEFAULT NULL COMMENT 'Round-trip Time',
     PRIMARY KEY (`logger_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8
