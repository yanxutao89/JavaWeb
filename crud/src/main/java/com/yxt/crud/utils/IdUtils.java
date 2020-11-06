package com.yxt.crud.utils;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 18:17
 */
public class IdUtils {

	public static Long nextId(){

		return System.nanoTime();
	}
}
