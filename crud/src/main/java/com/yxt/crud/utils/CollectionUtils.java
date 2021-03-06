package com.yxt.crud.utils;

import java.util.Arrays;
import java.util.Collection;

public class CollectionUtils {
	
	public static boolean isEmpty(Collection<?> collection) {
		
		if (null == collection || collection.size() == 0) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isEmpty(Object[] array) {
		return isEmpty(Arrays.asList(array));
	}

}
