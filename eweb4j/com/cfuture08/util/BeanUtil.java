package com.cfuture08.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.util.ReflectUtil;

public class BeanUtil {
	public static <T> Object getIdVal(T _t) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		String _idField = ORMConfigBeanUtil.getIdField(_t.getClass());
		ReflectUtil _ru = new ReflectUtil(_t);
		Method _idGetter = _ru.getGetter(_idField);
		return _idGetter.invoke(_t);
	}
}
