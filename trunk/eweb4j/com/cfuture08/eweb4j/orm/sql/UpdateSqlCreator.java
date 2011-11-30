package com.cfuture08.eweb4j.orm.sql;

import java.lang.reflect.Method;

import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.util.ReflectUtil;

/**
 * 生成更新语句
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class UpdateSqlCreator<T> {
	private T[] ts;

	public UpdateSqlCreator(T[] ts) {
		T[] tmp = null;
		if (ts != null && ts.length > 0) {
			tmp = ts;
		}
		this.ts = tmp;
	}

	public String[] update(String condition) {
		String[] sqls = null;
		if (this.ts != null && this.ts.length > 0) {
			sqls = new String[ts.length];
			for (int i = 0; i < ts.length; ++i) {
				sqls[i] = this.makeSQL(ts[i], condition);
			}
		}
		return sqls;
	}

	private String makeSQL(T t, String condition) {
		Class<?> clazz = t.getClass();
		String table = ORMConfigBeanUtil.getTable(clazz);
		return String.format("UPDATE %s %s ;", table, condition);
	}

	public String[] update() throws SqlCreateException {
		String[] sqls = new String[ts.length];
		for (int i = 0; i < ts.length; ++i) {
			sqls[i] = makeSQL(ts[i]);
		}

		return sqls;
	}

	public String[] update(String[] columns) throws SqlCreateException {
		String[] sqls = new String[ts.length];
		for (int i = 0; i < ts.length; ++i) {
			sqls[i] = makeSQL(ts[i], columns);
		}

		return sqls;
	}

	public String[] update(String[] columns, String[] values)
			throws SqlCreateException {
		String[] sqls = new String[ts.length];
		for (int i = 0; i < ts.length; ++i) {
			sqls[i] = makeSQL(ts[i], columns, values);
		}

		return sqls;
	}

	private String makeSQL(T t) throws SqlCreateException {

		Class<?> clazz = t.getClass();
		String table = ORMConfigBeanUtil.getTable(clazz);
		StringBuilder condition = new StringBuilder();
		StringBuilder values = new StringBuilder();
		ReflectUtil ru = new ReflectUtil(t);
		String[] columns = ORMConfigBeanUtil.getColumns(clazz);
		String[] fields = ORMConfigBeanUtil.getFields(clazz);
		String idColumn = ORMConfigBeanUtil.getIdColumn(clazz);
		String idField = ORMConfigBeanUtil.getIdField(clazz);
		condition.append(idColumn + " = ");
		try {
			Method idGetter = ru.getGetter(idField);
			if (idGetter == null) 
				throw new SqlCreateException("can not find id getter");
			
			condition.append("'" + idGetter.invoke(t) + "'");
			for (int i = 0; i < columns.length; i++) {
				String column = columns[i];
				String field = fields[i];
				// id 字段不允许插入表中
				if (idColumn != null && idColumn.equalsIgnoreCase(column))
					continue;
				Method m = ru.getGetter(field);
				if (m == null)
					continue;

				Object value = m.invoke(t);
				if (value == null)
					continue;

				if (values.length() > 0)
					values.append(",");

				values.append(column).append(" = '").append(value).append("'");
			}
		} catch (Exception e) {
			throw new SqlCreateException("" + e.toString());
		}

		return String.format("UPDATE %s SET %s WHERE %s ;", table, values,
				condition);
	}

	private String makeSQL(T t, String[] fields) throws SqlCreateException {
		Class<?> clazz = t.getClass();
		String table = ORMConfigBeanUtil.getTable(clazz);
		StringBuilder condition = new StringBuilder();
		StringBuilder values = new StringBuilder();
		ReflectUtil ru = new ReflectUtil(t);
		String[] columns = ORMConfigBeanUtil.getColumns(clazz, fields);
		String idColumn = ORMConfigBeanUtil.getIdColumn(clazz);
		String idField = ORMConfigBeanUtil.getIdField(clazz);
		Method idGetter = ru.getGetter(idField);
		if (idGetter == null)
			throw new SqlCreateException("can not find id getter.");

		try {
			condition.append(idColumn).append(" = '")
					.append(idGetter.invoke(t)).append("'");
		} catch (Exception e) {
			throw new SqlCreateException(idGetter + " invoke exception "
					+ e.toString());
		}

		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			String column = columns[i];
			Method m = ru.getGetter(field);
			if (m == null)
				continue;

			if (values.length() > 0)
				values.append(", ");

			values.append(column).append(" = '");
			try {
				values.append(m.invoke(t)).append("'");
			} catch (Exception e) {
				throw new SqlCreateException(idGetter + " invoke exception "
						+ e.toString());
			}
		}

		return String.format("UPDATE %s SET %s WHERE %s ;", table, values,
				condition);
	}

	private String makeSQL(T t, String[] fields, String[] values)
			throws SqlCreateException {
		Class<?> clazz = t.getClass();
		String table = ORMConfigBeanUtil.getTable(clazz);
		ReflectUtil ru = new ReflectUtil(t);
		String[] columns = ORMConfigBeanUtil.getColumns(clazz, fields);
		String idColumn = ORMConfigBeanUtil.getIdColumn(clazz);
		String idField = ORMConfigBeanUtil.getIdField(clazz);
		Method idGetter = ru.getGetter(idField);
		if (idGetter == null)
			throw new SqlCreateException("can not find id getter.");

		StringBuilder condition = new StringBuilder();
		try {
			condition.append(idColumn).append(" = '")
					.append(idGetter.invoke(t)).append("'");
		} catch (Exception e) {
			throw new SqlCreateException(idGetter + " invoke exception "
					+ e.toString());
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < columns.length; ++i) {
			String column = columns[i];
			if (sb.length() > 0)
				sb.append(", ");

			sb.append(column).append(" = '");
			sb.append(values[i]).append("'");
		}

		return String.format("UPDATE %s SET %s WHERE %s ;", table,
				sb.toString(), condition);
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
