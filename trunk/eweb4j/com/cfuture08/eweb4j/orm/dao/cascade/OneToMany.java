package com.cfuture08.eweb4j.orm.dao.cascade;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.eweb4j.orm.config.annotation.Many;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.dao.factory.DAOFactory;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;
import com.cfuture08.util.ExceptionInfoUtil;
import com.cfuture08.util.ReflectUtil;

/**
 * 
 * @author weiwei
 * 
 */
public class OneToMany {
	private String dsName;
	private Object t;
	private List<Field> fields;
	private ReflectUtil ru;
	private String idField;
	private String idVal;
	private Method idGetter;

	public OneToMany(String dsName) {
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
		// 主类的ID属性名
		this.idField = ORMConfigBeanUtil.getIdField(this.t.getClass());
		this.idGetter = ru.getGetter(idField);
		if (idGetter == null)
			throw new DAOException("can not find idGetter.");

		Object idVal = null;
		try {
			idVal = idGetter.invoke(this.t);
			this.idVal = idVal == null ? null : String.valueOf(idVal);
		} catch (Exception e) {
			throw new DAOException(idGetter + " invoke exception "
					+ ExceptionInfoUtil.toString(e));
		}

	}

	/**
	 * 一对多（主从）级联插入 1.获取ID 2.插入次对象
	 */
	public void insert() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;

		if (idVal == null || "0".equals(idVal) || "".equals(idVal))
			throw new DAOException(
					"this obj's idVal must be valid. not is [\"null\"|\"0\"|\"\"]");
		else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null)
			throw new DAOException(
					"this obj's idVal must be recored in dataBase.");

		for (Field f : fields) {
			Many ann = f.getAnnotation(Many.class);
			if (ann == null)
				continue;

			Method tarGetter = ru.getGetter(f.getName());
			if (tarGetter == null)
				throw new DAOException("can not find tarGetter.");
			
			List<?> tarList = null;

			try {
				tarList = (List<?>) tarGetter.invoke(t);
			} catch (Exception e) {
				throw new DAOException(tarGetter + " invoke exception "
						+ ExceptionInfoUtil.toString(e));
			}

			if (tarList == null)
				continue;
			for (int i = 0; i < tarList.size(); i++) {
				Object tarObj = tarList.get(i);
				String[] sqls = SqlFactory
						.getInsertSql(new Object[] { tarObj }).createWithFk(
								t.getClass());
				String sql = null;
				if (sqls != null)
					sql = sqls[0];
				// 将次对象一个一个插入到数据库
				String.valueOf(DAOFactory.getUpdateDAO(dsName)
						.updateBySQLWithArgs(sql, idVal));
			}
		}
	}

	/**
	 * 一对多（主从）级联删除 1.前提条件必须主对象要存在于数据库中
	 * 2.检查当前主对象中的关联对象，如果关联对象为空，则删除所有与主对象有关的关联关系。
	 * 3.如果当前主对象中含有关联对象，则删除这些关联对象与主对象的关系
	 */
	public void delete() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;

		if (idVal == null || "0".equals(idVal) || "".equals(idVal))
			throw new DAOException(
					"this obj's idVal must be valid. not is [\"null\"|\"0\"|\"\"]");
		else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null)
			throw new DAOException(
					"this obj's idVal must be recored in dataBase.");

		for (Field f : fields) {
			Many ann = f.getAnnotation(Many.class);
			if (ann == null)
				continue;

			Class<?> tarClass = ann.target();
			String tarTable = ORMConfigBeanUtil.getTable(tarClass);
			String column = ann.column();
			Method tarGetter = ru.getGetter(f.getName());
			List<?> tarList = null;

			try {
				tarList = (List<?>) tarGetter.invoke(t);
			} catch (Exception e) {
				throw new DAOException(tarGetter + " invoke exception "
						+ ExceptionInfoUtil.toString(e));
			}

			if (tarList == null || tarList.size() == 0) {
				// 当关联对象为空的时候，删除所有关联对象
				// delete from {tarTable} where {column} = {idVal}
				String format = "delete from %s where %s = ? ";
				String sql = String.format(format, tarTable, column);
				DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(sql, idVal);
			} else {
				for (int i = 0; i < tarList.size(); i++) {
					Object tarObj = tarList.get(i);
					if (tarObj == null)
						continue;
					// 检查是否该对象真的与主对象有关联且存在数据库
					// select * from {tarTable} where {tarIdColumn} =
					// {tarIdVal} and {column} = {idVal}
					String format = "select * from %s where %s = ? and %s = ? ";

					ReflectUtil tarRu = new ReflectUtil(tarObj);
					String tarIdColumn = ORMConfigBeanUtil
							.getIdColumn(tarClass);
					String tarIdField = ORMConfigBeanUtil.getIdField(tarClass);
					Method tarIdGetter = tarRu.getGetter(tarIdField);
					
					if (tarIdGetter == null)
						throw new DAOException("can not find tarIdGetter.");
					
					Object tarIdValObj = null;

					try {
						tarIdValObj = tarIdGetter.invoke(tarObj);
					} catch (Exception e) {
						throw new DAOException(tarIdGetter
								+ " invoke exception " + ExceptionInfoUtil.toString(e));
					}

					if (tarIdValObj == null)
						continue;
					
					String tarIdVal = String.valueOf(tarIdValObj);
					if ("0".equals(tarIdVal) || "".equals(tarIdVal))
						continue;

					String sql = String.format(format, tarTable, tarIdColumn,
							column);
					
					boolean canDel = DAOFactory.getSelectDAO(dsName)
							.selectBySQL(tarClass, sql, tarIdVal, idVal) == null ? false
							: true;
					
					if (canDel)
						DAOFactory.getDeleteDAO(dsName).delete(tarObj);
				}
			}
		}
	}

	/**
	 * 一对多（主从）级联查询
	 */
	public void select() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;
		if (idVal == null || "0".equals(idVal) || "".equals(idVal))
			return;
		else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null)
			return;

		// select * from {tarTable} where {column} = {idVal}
		String format = "select * from %s where %s = ? ";

		for (Field f : fields) {
			Many ann = f.getAnnotation(Many.class);
			if (ann == null)
				continue;
			Class<?> tarClass = ann.target();
			String tarTable = ORMConfigBeanUtil.getTable(tarClass);
			String column = ann.column();
			String sql = String.format(format, tarTable, column);
			List<?> tarList = DAOFactory.getSelectDAO(dsName).selectBySQL(
					tarClass, sql, idVal);
			if (tarList == null)
				continue;

			Method tarSetter = ru.getSetter(f.getName());
			if (tarSetter == null)
				continue;
			try {
				tarSetter.invoke(t, tarList);
			} catch (Exception e) {

				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
	}
}
