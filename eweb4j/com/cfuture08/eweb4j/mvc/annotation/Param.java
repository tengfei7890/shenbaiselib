package com.cfuture08.eweb4j.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * url?后面的参数,用在方法参数上
 * @author weiwei
 *
 */
@Inherited
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.PARAMETER) 
public @interface Param {
	String value();
	String init() default "";
}
