package com.yxt.crud.utils;

import java.util.Arrays;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/04/06 9:04
 */
public class MyBitMapUtils {

	private byte[] bytes;
	private int initSize;

	public MyBitMapUtils(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Parameter of 'size' must be positive!") ;
		}
		initSize = (size >> 3) + 1;
		bytes = new byte[initSize];
	}

	public void set(int number) {
		int index = number >> 3;
		int position = number & 0x07;
		bytes[index] |= 1 << position;
	}

	public boolean contains(int number) {
		int index = number >> 3;
		int position = number & 0x07;
		return (bytes[index] & (1 << position)) != 0;
	}

	@Override
	public String toString() {
		return "MyBitMapUtils{" +
				"bytes=" + Arrays.toString(bytes) +
				", initSize=" + initSize +
				'}';
	}

	public static void main(String[] args) {
		MyBitMapUtils myBitMap = new MyBitMapUtils(32);
		myBitMap.set(30);
		myBitMap.set(13);
		myBitMap.set(24);
		System.out.println(myBitMap.contains(24));
		System.out.println(myBitMap.contains(2));
	}

}
