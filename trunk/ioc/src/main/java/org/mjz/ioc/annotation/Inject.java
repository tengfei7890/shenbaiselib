package org.mjz.ioc.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * 组件注解，标记组件实例需注入的组件
 * @author EX-MOJIEZHONG001
 *
 */
@Target({FIELD,PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface Inject {
	String value() default "";
}
