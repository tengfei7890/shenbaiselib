package org.mjz.ioc.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 组件注解，标记组件实例化时用的构造函数<br/>
 * 一个类只能标记一处
 * @author EX-MOJIEZHONG001
 *
 */
@Target(CONSTRUCTOR)
@Retention(RUNTIME)
@Documented
public @interface Constructor {

}
