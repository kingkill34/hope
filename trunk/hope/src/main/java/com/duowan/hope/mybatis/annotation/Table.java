package com.duowan.hope.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	Class<?> value();

	String tableSuffix() default "";

	String tableSeparator() default "_";

	int currenyPage() default 1;

	int page() default 1;

}
