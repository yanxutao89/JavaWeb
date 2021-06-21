package com.user.annotations;

import java.lang.annotation.*;

/**
 * Marker for crud logger
 * @author yanxt7
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MsLog {

	/**
	 * target method name
	 * @return
	 */
	String value();

}
