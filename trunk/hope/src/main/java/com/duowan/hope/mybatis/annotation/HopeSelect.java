package com.duowan.hope.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HopeSelect {

	String value() default "";

	String orderByASC() default "";

	String orderByDESC() default "";

	String groupBy() default "";

}
