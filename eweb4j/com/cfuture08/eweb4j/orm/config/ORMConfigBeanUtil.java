package com.cfuture08.eweb4j.orm.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.cache.ORMConfigBeanCache;
import com.cfuture08.eweb4j.orm.config.annotation.One;
import com.cfuture08.eweb4j.orm.config.annotation.Table;
import com.cfuture08.eweb4j.orm.config.bean.ORMConfigBean;
import com.cfuture08.eweb4j.orm.config.bean.Property;
import com.cfuture08.util.ReflectUtil;

public class ORMConfigBeanUtil {

	public static <T> Object getIdVal(T _t) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		String _idField = ORMConfigBeanUtil.getIdField(_t.getClass());
		ReflectUtil _ru = new ReflectUtil(_t);
		Method _idGetter = _ru.getGetter(_idField);
		return _idGetter.invoke(_t);
	}

	public static List<String> getFkColumn(Class<?> clazz, Class<?> fkClass) {
		List<String> result = new ArrayList<String>();
		Table tableAnn = clazz.getAnnotation(Table.class);
		if (tableAnn == null)
			return null;

		ReflectUtil ru;
		try {
			ru = new ReflectUtil(clazz);
			Field[] fields = ru.getFields();
			for (Field f : fields) {
				One oneAnn = f.getAnnotation(One.class);
				if (oneAnn == null)
					continue;
				Class<?> cls = f.getType();
				if (fkClass != null) {
					if (fkClass.isAssignableFrom(cls)) {
						String fk = oneAnn.column();
						result.add(fk);
						break;
					}
				} else {
					String fk = oneAnn.column();
					result.add(fk);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result.isEmpty() ? null : result;
	}

	public static String getIdColumn(Class<?> clazz) {
		return getId(clazz, 1);
	}

	public static String getIdField(Class<?> clazz) {
		return getId(clazz, 2);
	}

	/**
	 * 获取自增长的主键名字
	 * 
	 * @param clazz
	 * @param type
	 *            1的时候获取的是数据库字段名，2的时候获取的是java类的属性名
	 * @return
	 */
	public static String getId(Class<?> clazz, int type) {
		String pk = "id";
		ORMConfigBean ormBean = ORMConfigBeanCache.get(clazz);
		if (ormBean != null) {
			for (Property property : ormBean.getProperty()) {
				if ("true".equals(property.getPk())
						|| "1".equals(property.getPk())
						|| "true".equals(property.getAutoIncrement())
						|| "1".equals(property.getAutoIncrement())) {
					if (1 == type)
						pk = property.getColumn();
					else if (2 == type)
						pk = property.getName();

					break;
				}
			}

		}

		// System.out.println("cls-"+clazz+"-type-"+type+"-pk->"+pk);
		return pk;
	}

	/**
	 * get table name
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getTable(Class<?> clazz) {
		ORMConfigBean ormBean = ORMConfigBeanCache.get(clazz);
		String table = ormBean == null ? clazz.getSimpleName() : ormBean
				.getTable();
		return table;
	}

	public static String[] getColumns(Class<?> clazz) {
		return getColumnsOrFields(clazz, null, 3);
	}

	public static String[] getFields(Class<?> clazz) {
		return getColumnsOrFields(clazz, null, 4);
	}

	public static String[] getColumns(Class<?> clazz, String[] fields) {
		return getColumnsOrFields(clazz, fields, 1);
	}

	public static String getColumn(Class<?> clazz, String field) {
		return getColumns(clazz, new String[] { field })[0];
	}

	public static String[] getFields(Class<?> clazz, String[] columns) {
		return getColumnsOrFields(clazz, columns, 2);
	}

	public static String getField(Class<?> clazz, String column) {
		return getColumnsOrFields(clazz, new String[] { column }, 2)[0];
	}

	/**
	 * get columns through fields
	 * 
	 * @param clazz
	 * @param fields
	 * @param type
	 *            1.getColumns 2.getFields 3.allColumns 4.allFields
	 * 
	 * @return
	 */
	private static String[] getColumnsOrFields(Class<?> clazz, String[] strs,
			int type) {
		if (strs == null)
			strs = new String[] { "" };
		String[] result = strs.clone();
		List<String> list = new ArrayList<String>();
		ORMConfigBean ormBean = ORMConfigBeanCache.get(clazz);
		if (ormBean != null) {
			// String idColumn = getIdColumn(clazz);
			for (int i = 0; i < strs.length; i++) {
				boolean finished = false;
				List<Property> properties = ormBean.getProperty();
				for (Property p : properties) {
					if (finished)
						break;
					// if ((3 == type || 4 == type)
					// && idColumn.equals(p.getColumn()))
					// continue;
					switch (type) {
					case 1:
						if (p.getName().equals(strs[i])) {
							result[i] = p.getColumn();
							finished = true;
						}
						break;
					case 2:
						if (p.getColumn().equals(strs[i])) {
							result[i] = p.getName();
							finished = true;
						}
						break;
					case 3:
						list.add(p.getColumn());
						break;
					case 4:
						list.add(p.getName());
					}
				}
			}
		}

		return list.isEmpty() ? result : list.toArray(new String[] {});
	}
}
