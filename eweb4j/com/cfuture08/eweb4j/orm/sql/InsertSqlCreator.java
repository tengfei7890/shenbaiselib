package com.cfuture08.eweb4j.orm.sql;

import java.lang.reflect.Method;
import java.util.List;

import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.util.ReflectUtil;

/**
 * 生成插入语句
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class InsertSqlCreator<T> {
	private T[] ts;

	public InsertSqlCreator(T... ts) {
		T[] tmp = null;
		if (ts != null && ts.length > 0) {
			tmp = ts.clone();
		}
		this.ts = tmp;
	}

	public String[] create() throws SqlCreateException {
		return this.create(false, null, null);
	}

	public String[] createWithFk(Class<?> clazz) throws SqlCreateException {
		return create(true, clazz, null);
	}

	public String[] createByFields(String[]... fields)
			throws SqlCreateException {
		String[] sqls = new String[ts.length];
		for (int i = 0; i < ts.length; ++i) {
			T t = ts[i];
			ReflectUtil ru = new ReflectUtil(t);
			Class<?> clazz = t.getClass();
			String table = ORMConfigBeanUtil.getTable(clazz);
			StringBuilder columnSB = new StringBuilder();
			StringBuilder valueSB = new StringBuilder();
			for (int j = 0; j < fields[i].length; ++j) {
				String name = fields[i][j];
				Method m = ru.getGetter(name);
				if (m == null)
					continue;
				Object value = null;
				try {
					value = m.invoke(t);
				} catch (Exception e) {
					throw new SqlCreateException(m + " invoke exception "
							+ e.toString());
				}

				if (value == null)
					continue;

				String column = ORMConfigBeanUtil.getColumn(clazz, name);
				if (valueSB.length() > 0) {
					columnSB.append(",");
					valueSB.append(",");
				}

				columnSB.append(column);
				valueSB.append("'").append(value).append("'");
			}

			sqls[i] = String.format("INSERT INTO %s(%s) VALUES(%s) ;", table,
					columnSB.toString(), valueSB.toString());
		}

		return sqls;
	}

	public String[] create(String condition) throws SqlCreateException {
		return create(false, null, condition);
	}

	public String[] create(boolean isInsertFk, Class<?> fkClass,
			String condition) throws SqlCreateException {
		String[] sqls = new String[ts.length];
		for (int index = 0; index < ts.length; ++index) {
			String table = null;
			StringBuilder columnSB = new StringBuilder();
			StringBuilder valueSB = new StringBuilder();
			T t = ts[index];
			Class<?> clazz = t.getClass();
			table = ORMConfigBeanUtil.getTable(clazz);
			String[] fields = ORMConfigBeanUtil.getFields(clazz);
			String[] _columns = ORMConfigBeanUtil.getColumns(clazz);
			ReflectUtil ru = new ReflectUtil(t);
			for (int i = 0; i < _columns.length; i++) {
				String column = _columns[i];
				String name = fields[i];

				Method m = ru.getGetter(name);
				if (m == null)
					continue;
				
				Object value = null;

				try {
					value = m.invoke(t);
				} catch (Exception e) {
					throw new SqlCreateException(m + " invoke exception "
							+ e.toString());
				}

				if (value == null)
					continue;

				String idColumn = ORMConfigBeanUtil.getIdColumn(t.getClass());
				// id 字段不允许插入表中
				if (idColumn != null && idColumn.equalsIgnoreCase(column))
					continue;
				
				if (columnSB.length() > 0)
					columnSB.append(",");

				columnSB.append(column);
				
				
				
				if (valueSB.length() > 0)
					valueSB.append(",");

				valueSB.append("'").append(value).append("'");

			}

			if (isInsertFk) {
				// 如果要插入外键
				List<String> fks = ORMConfigBeanUtil
						.getFkColumn(clazz, fkClass);
				if (fks != null && fks.size() > 0) {
					if (fks.size() == 1) {
						columnSB.append(",").append(fks.get(0));
						valueSB.append(",?");
					} else {
						for (String s : fks) {
							columnSB.append(",").append(s);
							valueSB.append(",?");
						}
					}
				}
			}

			String format = "INSERT INTO ${table}(${columns}) VALUES(${values}) ${condition} ;";
			format = format.replace("${table}", table);
			format = format.replace("${columns}", columnSB.toString());
			format = format.replace("${values}", valueSB.toString());

			if (condition != null)
				format = format.replace("${condition}", " WHERE " + condition);
			else
				format = format.replace("${condition}", "");

			sqls[index] = format;
		}

		return sqls;
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
