package org.mjz.ioc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 组件注解，标记组件实例是否需生命周期管理
 * @author EX-MOJIEZHONG001
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Stateful {

}
