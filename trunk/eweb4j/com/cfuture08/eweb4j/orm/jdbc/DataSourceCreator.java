package com.cfuture08.eweb4j.orm.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.sql.DataSource;

import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.orm.dao.config.bean.DBInfoConfigBean;
import com.cfuture08.eweb4j.orm.dao.config.bean.Property;
import com.cfuture08.util.ReflectUtil;

/**
 * 创建数据源
 * 
 * @author weiwei
 * 
 */
public class DataSourceCreator {

	public static DataSource getDataSource(DBInfoConfigBean dbInfo)
			throws NumberFormatException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			InstantiationException, ClassNotFoundException {

		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		Class<?> cls = Class.forName(cb.getOrm().getDataSource());
		DataSource ds = (DataSource) cls.newInstance();

		List<Property> properties = dbInfo.getProperties();

		for (Property property : properties) {
			String name = property.getKey();
			String value = property.getValue();
			ReflectUtil ru2 = new ReflectUtil(ds);
			Method m2 = ru2.getSetter(name);
			if (m2 == null)
				continue;

			Class<?> clazz = m2.getParameterTypes()[0];
			if (int.class.isAssignableFrom(clazz)
					|| Integer.class.isAssignableFrom(clazz)
					|| long.class.isAssignableFrom(clazz)
					|| Long.class.isAssignableFrom(clazz))
				m2.invoke(ds, Integer.parseInt(value));
			else if (float.class.isAssignableFrom(clazz)
					|| Float.class.isAssignableFrom(clazz)
					|| double.class.isAssignableFrom(clazz)
					|| Double.class.isAssignableFrom(clazz))
				m2.invoke(ds, Float.parseFloat(value));
			else if (boolean.class.isAssignableFrom(clazz)
					|| Boolean.class.isAssignableFrom(clazz))
				m2.invoke(ds, Boolean.parseBoolean(value));
			else
				m2.invoke(ds, value);

		}
		
		return ds;
	}
}
