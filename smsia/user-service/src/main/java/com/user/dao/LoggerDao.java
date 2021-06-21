package com.user.dao;

import com.user.model.LoggerPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LoggerDao {

    List<LoggerPojo> selectLogger(Map queryMap);

    Integer hasRequestNo(@Param("requestNo") Long requestNo);

    Integer insertLogger(LoggerPojo record);

    Integer insertLoggerBatch(List records);

    Integer updateLogger(LoggerPojo record);

    Integer updateLoggerBatch(List records);

}
