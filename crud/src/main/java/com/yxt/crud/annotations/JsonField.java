package com.yxt.crud.annotations;

import java.lang.annotation.*;

/**
 * Marker for field deserialization
 * @author yanxt7
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface JsonField {

	/**
	 * target property name
	 * @return
	 */
	String value();

	/**
	 * alternative keys
	 * @return
	 */
	String[] aliasKeys() default {};

}
