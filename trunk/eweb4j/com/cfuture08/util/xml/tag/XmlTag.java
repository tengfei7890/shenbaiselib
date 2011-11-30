package com.cfuture08.util.xml.tag;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注释标签
 * 用于对象属性注释
 * @author CFuture.aw
 * @since v1.0.0
 *
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlTag {
	String type() default XmlTagType.elementType;
	String value() default "";
	boolean canWrite() default true;
	boolean canRead() default true;
}
