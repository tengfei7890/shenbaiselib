package com.cfuture08.eweb4j.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cfuture08.eweb4j.mvc.action.RequestMethodType;
@Inherited
@Documented  
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE,ElementType.METHOD })
public @interface RequestMapping {
	String value();//url
	String method() default RequestMethodType.GET;//http method
	String showValErr() default "alert";//how to show validate message
}
