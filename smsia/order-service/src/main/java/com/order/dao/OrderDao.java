package com.order.dao;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/19 17:18
 */
public interface OrderDao {

    List<Map> selectOrderList(Map map);

}
