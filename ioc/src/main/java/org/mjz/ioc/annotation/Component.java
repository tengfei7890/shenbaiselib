package org.mjz.ioc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.mjz.ioc.ComponentType;

/**
 * 组件注解，标记组件名及作用域
 * @author EX-MOJIEZHONG001
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Component{
	String value() default "";
	ComponentType scope() default ComponentType.Singleton;

}
