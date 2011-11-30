package com.cfuture08.eweb4j.orm;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cfuture08.eweb4j.cache.ORMConfigBeanCache;
import com.cfuture08.eweb4j.orm.config.bean.ORMConfigBean;
import com.cfuture08.eweb4j.orm.config.bean.Property;
import com.cfuture08.util.ReflectUtil;

/**
 * 将从数据库查询出来的结果集映射成java对象
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class RowMapping {

	public static <T> T mapOneRow(ResultSet rs, Class<T> cls) throws Exception {
		return mapOneRow(rs, null, cls);
	}

	public static <T> List<T> mapRows(ResultSet rs, Class<T> cls)
			throws Exception {
		return mapRows(rs, null, cls);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> mapRows(ResultSet rs, ResultSetMetaData rsmd,
			Class<T> cls) throws Exception {
		List<T> list = new ArrayList<T>();
		T t = null;
		if (rs != null) {
			if (Map.class.isAssignableFrom(cls)) {
				List<Map<String, Object>> _list = new ArrayList<Map<String, Object>>();
				rsmd = rs.getMetaData();
				List<String> columns = new ArrayList<String>();
				for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
					columns.add(rsmd.getColumnName(i));
				}
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 1; i <= columns.size(); ++i) {
						String name = columns.get(i - 1);
						map.put(name, rs.getObject(name));
					}
					_list.add(map);
				}

				return (List<T>) (_list.isEmpty() ? null : _list);
			} else {
				while (rs.next()) {
					t = cls.newInstance();
					ReflectUtil ru = new ReflectUtil(t);
					ORMConfigBean ormBean = ORMConfigBeanCache.get(cls);

					for (Iterator<Property> it = ormBean.getProperty()
							.iterator(); it.hasNext();) {
						Property p = it.next();
						String type = p.getType();
						if (type != null) {
							Method m = ru.getSetter(p.getName());
							if (m != null) {
								Object value = rs.getObject(p.getColumn());
								if (value != null) {
									String v = String.valueOf(value);
									if (v == null) {
										v = "";
									}
									if ("int".equalsIgnoreCase(type)
											|| "java.lang.Integer"
													.equalsIgnoreCase(type)) {
										if ("".equals(v.trim())) {
											v = "0";
										}
										m.invoke(t, Integer.parseInt(v));
									} else if ("long".equalsIgnoreCase(type)
											|| "java.lang.Long"
													.equalsIgnoreCase(type)) {
										if ("".equals(v.trim())) {
											v = "0";
										}
										m.invoke(t, Long.parseLong(v));
									} else if ("float".equalsIgnoreCase(type)
											|| "java.lang.Float"
													.equalsIgnoreCase(type)) {
										if ("".equals(v.trim())) {
											v = "0.0";
										}
										m.invoke(t, Float.parseFloat(v));
									} else if ("double".equalsIgnoreCase(type)
											|| "java.lang.Double"
													.equalsIgnoreCase(type)) {
										if ("".equals(v.trim())) {
											v = "0.0";
										}
										
										m.invoke(t, Float.parseFloat(v));
									} else if ("string".equalsIgnoreCase(type)
											|| "java.lang.String"
													.equalsIgnoreCase(type)) {
										m.invoke(t, v);
									} else if ("date".equalsIgnoreCase(type)
											|| "java.sql.Date"
													.equalsIgnoreCase(type)
											|| "java.util.Date"
													.equalsIgnoreCase(type)) {
										m.invoke(t, value);
									} else if (!"".equals(type)) {
										m.invoke(t, String.valueOf(value));
									}
								}
							}
						}
					}
					list.add(t);
				}
			}
		}

		return list.isEmpty() ? null : list;
	}

	public static <T> T mapOneRow(ResultSet rs, ResultSetMetaData rsmd,
			Class<T> cls) throws Exception {
		List<T> list = mapRows(rs, rsmd, cls);
		return list == null ? null : list.get(0);
	}

}
