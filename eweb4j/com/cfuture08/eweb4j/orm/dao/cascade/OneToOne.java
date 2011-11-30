package com.cfuture08.eweb4j.orm.dao.cascade;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.eweb4j.orm.config.annotation.One;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.dao.factory.DAOFactory;
import com.cfuture08.util.ExceptionInfoUtil;
import com.cfuture08.util.ReflectUtil;

public class OneToOne {
	private String dsName;
	private Object t;
	private List<Field> fields;
	private ReflectUtil ru;
	private String table;
	private String idColumn;
	private String idField;
	private String idVal;
	private Method idGetter;

	public OneToOne(String dsName) {
		this.dsName = dsName;
	}

	/**
	 * 初始化
	 * 
	 * @param t
	 * @param fields
	 * @throws DAOException
	 */
	public void init(Object t, List<Field> fields) throws DAOException {
		this.t = t;
		this.fields = fields;
		this.ru = new ReflectUtil(this.t);
		this.table = ORMConfigBeanUtil.getTable(t.getClass());
		// 主类的ID属性名
		this.idField = ORMConfigBeanUtil.getIdField(this.t.getClass());
		this.idColumn = ORMConfigBeanUtil.getIdColumn(this.t.getClass());
		this.idGetter = ru.getGetter(idField);
		Object idVal;
		try {
			idVal = idGetter.invoke(this.t);
			this.idVal = idVal == null ? null : String.valueOf(idVal);
		} catch (Exception e) {

			throw new DAOException("" + ExceptionInfoUtil.toString(e));
		}
	}

	/**
	 * 多对一级联更新（前提要存在当前对象在数据库中） 
	 * 1.先检查新的主对象tarIdVal是否在数据库中有效 2.然后将这个值更新到当前对象所在的记录中
	 */
	public void update() {
		if (this.fields == null || this.fields.size() == 0)
			return;
		if (idVal == null || "0".equals(idVal) || "".equals(idVal)) {
			return;
		} else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null) {
			// 检查一下当前对象的ID是否存在于数据库
			return;
		}
		// "update {table} set {column} = {tarIdVal} where {id} = {idVal}"
		String format = "update %s set %s = ? where %s = ? ";
		for (Field f : fields) {
			One ann = f.getAnnotation(One.class);
			if (ann == null)
				continue;
			Class<?> tarClass = f.getType();
			String tarIdField = ORMConfigBeanUtil.getIdField(tarClass);
			Method tarGetter = ru.getGetter(f.getName());
			try {
				Object tarObj = tarGetter.invoke(t);
				if (tarObj == null)
					continue;
				
				ReflectUtil tarRu = new ReflectUtil(tarObj);
				Method tarIdGetter = tarRu.getGetter(tarIdField);
				Object tarIdValObj = tarIdGetter.invoke(tarObj);
				if (tarIdValObj == null)
					continue;
				
				String column = ann.column();

				String sql = String.format(format, table, column, idColumn);
				DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(
						sql, tarIdValObj, idVal);
				
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
	}

	/**
	 * 多对一级联查询 1.获取当前idVal，然后作为条件查询出其外键值，接着通过其外键值查出主对象数据，注入到当前
	 */
	public void select() {
		if (this.fields == null || this.fields.size() == 0)
			return;
		if (idVal == null || "0".equals(idVal) || "".equals(idVal)) {
			return;
		} else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null) {
			// 检查一下当前对象的ID是否存在于数据库
			return;
		}

		for (Field f : fields) {
			One ann = f.getAnnotation(One.class);
			if (ann == null)
				continue;
			Class<?> tarClass = f.getType();
			// select * from {tarTable} where {tarIdColumn} = (select {column}
			// from {table} where {idColumn} = {idVal})
			String format = "select * from %s where %s = (select %s from %s where %s = %s )";
			String tarTable = ORMConfigBeanUtil.getTable(tarClass);
			String tarIdColumn = ORMConfigBeanUtil.getIdColumn(tarClass);
			String column = ann.column();
			String sql = String.format(format, tarTable, tarIdColumn, column,
					table, idColumn, idVal);
			List<?> tarList = DAOFactory.getSelectDAO(dsName).selectBySQL(
					tarClass, sql);
			if (tarList == null || tarList.size() == 0)
				continue;
			Object tarObj = tarList.get(0);
			Method tarSetter = ru.getSetter(f.getName());
			try {
				tarSetter.invoke(t, tarObj);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
	}
}
