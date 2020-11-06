package com.yxt.crud.mapper;

import com.yxt.crud.bean.KeyValuePojo;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/06 21:15
 */
public interface IKeyValueMapper {

	List<KeyValuePojo> selectKeyValue(Map queryMap);

}
