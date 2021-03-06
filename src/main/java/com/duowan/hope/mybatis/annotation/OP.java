package com.duowan.hope.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作符
 * 
 * @author frankie
 * @date 2016年6月16日 上午10:40:39
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface OP {

	/**
	 * 
	 * @return
	 */
	String value() default "";

	boolean isNull() default false;

	boolean isNotNull() default false;

	boolean isUpdate() default false;

}
