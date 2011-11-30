package com.cfuture08.eweb4j.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Inherited
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD) 
public @interface Result {
	String[] name() default "success";
	String[] type() default "forward";
	String[] value();
}
