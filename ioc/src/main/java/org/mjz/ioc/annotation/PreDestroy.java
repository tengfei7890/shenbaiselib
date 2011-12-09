package org.mjz.ioc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 组件注解，标记组件实例生命周期结束的方法, 方法为public且无入参<br/>
 * 前提组件标记了Stateful注解
 * @see Stateful
 * @author EX-MOJIEZHONG001
 *
 */
@Documented
@Retention (RUNTIME)
@Target(METHOD)
public @interface PreDestroy {

}
