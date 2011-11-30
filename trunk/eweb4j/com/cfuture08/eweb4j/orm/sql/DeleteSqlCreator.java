package com.cfuture08.eweb4j.orm.sql;

import java.lang.reflect.Method;

import com.cfuture08.eweb4j.cache.ORMConfigBeanCache;
import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.eweb4j.orm.config.bean.ORMConfigBean;
import com.cfuture08.util.ReflectUtil;

/**
 * 生成删除语句
 * 
 * @author cfuture.wg
 * @since v1.a.0
 */
public class DeleteSqlCreator<T> {
	private T[] ts;

	public DeleteSqlCreator() {
	}

	public DeleteSqlCreator(T... ts) {
		T[] tmp = null;
		if (ts != null && ts.length > 0) {
			tmp = ts.clone();
		}
		this.ts = tmp;
	}

	public String deleteWhere(String condition) {
		if (this.ts != null && this.ts.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (T t : this.ts) {
				sb.append(this.makeSQL(t, condition));
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	private String makeSQL(T t, String condition) {
		ORMConfigBean ormBean = ORMConfigBeanCache.get(t.getClass());
		String table = ormBean != null ? ormBean.getTable() : t.getClass()
				.getSimpleName();
		return String.format("DELETE FROM %s WHERE %s ;", table, condition);
	}

	public String[] delete() throws SqlCreateException {
		String[] sqls = new String[ts.length];
		for (int i = 0; i < ts.length; i++) {
			sqls[i] = this.makeSQL(ts[i]);
		}
		return sqls;
	}

	public String[] delete(String[] fields, String[] values)
			throws SqlCreateException {
		String[] sqls = new String[ts.length];
		for (int i = 0; i < ts.length; i++) {
			sqls[i] = this.makeSQL(ts[i], fields, values);
		}
		return sqls;
	}

	public String[] delete(String... fields) throws SqlCreateException {
		String[] sqls = new String[ts.length];
		for (int i = 0; i < ts.length; i++) {
			sqls[i] = this.makeSQL(ts[i], fields);
		}
		return sqls;
	}

	private String makeSQL(T t) throws SqlCreateException {
		Class<?> clazz = t.getClass();
		String idField = ORMConfigBeanUtil.getIdField(clazz);
		ReflectUtil ru = new ReflectUtil(t);
		Method method = ru.getGetter(idField);
		if (method == null) {
			throw new SqlCreateException("can not find id getter.");
		}
		String table = ORMConfigBeanUtil.getTable(clazz);
		String idColumn = ORMConfigBeanUtil.getIdColumn(clazz);

		StringBuilder condition = new StringBuilder();
		condition.append(idColumn + " = ");

		Object value = null;

		try {
			value = method.invoke(t);
		} catch (Exception e) {
			throw new SqlCreateException(method + " invoke exception "
					+ e.toString());
		}

		condition.append("'" + value + "'");

		return String.format("DELETE FROM %s WHERE %s ;", table, condition);
	}

	private String makeSQL(T t, String[] fields, String[] values)
			throws SqlCreateException {
		Class<?> clazz = t.getClass();
		String table = ORMConfigBeanUtil.getTable(clazz);

		StringBuilder condition = new StringBuilder();
		String[] columns = ORMConfigBeanUtil.getColumns(clazz, fields);
		for (int i = 0; i < columns.length; ++i) {
			if (condition.length() > 0) {
				condition.append(" AND ");
			}

			condition.append(columns[i] + " = ");
			condition.append("'" + values[i] + "'");

		}
		return String.format("DELETE FROM %s WHERE %s ;", table, condition);
	}

	private String makeSQL(T t, String... fields) throws SqlCreateException {
		Class<?> clazz = t.getClass();
		String table = ORMConfigBeanUtil.getTable(clazz);
		StringBuilder condition = new StringBuilder();
		ReflectUtil ru = new ReflectUtil(t);

		for (int i = 0; i < fields.length; i++) {
			if (condition.length() > 0)
				condition.append(" AND ");

			Method m = ru.getGetter(fields[i]);
			if (m == null)
				continue;

			String column = ORMConfigBeanUtil.getColumn(clazz, fields[i]);
			condition.append(column + " = ");

			Object value = null;

			try {
				value = m.invoke(t);
			} catch (Exception e) {
				throw new SqlCreateException(m + " invoke exception "
						+ e.toString());
			}

			condition.append("'" + value + "'");

		}
		return String.format("DELETE FROM %s WHERE %s ;", table, condition);
	}

	public T[] getTs() {
		T[] tmp = null;
		if (ts != null && ts.length > 0) {
			tmp = ts.clone();
		}
		return tmp;
	}

	public void setTs(T[] ts) {
		T[] tmp = null;
		if (ts != null && ts.length > 0) {
			tmp = ts.clone();
		}
		this.ts = tmp;
	}
}
