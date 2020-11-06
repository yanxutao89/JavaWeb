package com.yxt.crud.annotations;

import java.lang.annotation.*;

/**
 * Marker for field deserialization
 * @author yanxt7
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Logger {

	/**
	 * target method name
	 * @return
	 */
	String value();

}
