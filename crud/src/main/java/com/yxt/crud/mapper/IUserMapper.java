package com.yxt.crud.mapper;

import com.yxt.crud.annotations.Converter;
import com.yxt.crud.bean.UserPojo;
import com.yxt.crud.interceptor.UserStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 15:40
 */
public interface IUserMapper extends IBaseMapper {

	@Converter(properties = {"status"}, enums = UserStatusEnum.class)
	List<Map> selectUsers(Map queryMap);

	Integer insertUser(UserPojo userPojo);

	Integer insertUserBatch(List records);

	Integer updateUser(UserPojo userPojo);

	Integer updateUserBatch(List records);

}
