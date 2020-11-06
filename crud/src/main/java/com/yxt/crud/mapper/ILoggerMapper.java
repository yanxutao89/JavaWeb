package com.yxt.crud.mapper;


import com.yxt.crud.bean.LoggerPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/06 15:50
 */
public interface ILoggerMapper extends IBaseMapper {

	List<LoggerPojo> selectLogger(Map queryMap);

	Integer hasRequestNo(@Param("requestNo") Long requestNo);

	Integer insertLogger(LoggerPojo record);

	Integer insertLoggerBatch(List records);

	Integer updateLogger(LoggerPojo record);

	Integer updateLoggerBatch(List records);

}
