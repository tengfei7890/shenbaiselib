package com.cfuture08.eweb4j.orm.dao.cascade;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.eweb4j.orm.config.annotation.ManyMany;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.dao.factory.DAOFactory;
import com.cfuture08.util.ExceptionInfoUtil;
import com.cfuture08.util.ReflectUtil;

public class ManyToMany {
	private String dsName;
	private Object t;
	private List<Field> fields;
	ReflectUtil ru;
	String idField;
	String fromVal;
	Method idSetter;
	Method idGetter;

	public ManyToMany(String dsName) {
		this.dsName = dsName;
	}

	public void init(Object t, List<Field> fields) throws DAOException {
		this.t = t;
		this.fields = fields;
		this.ru = new ReflectUtil(this.t);
		// 主类的ID属性名
		this.idField = ORMConfigBeanUtil.getIdField(this.t.getClass());
		this.idSetter = ru.getSetter(idField);
		if (this.idSetter == null)
			throw new DAOException("can not get idSetter.");

		this.idGetter = ru.getGetter(idField);
		if (this.idGetter == null)
			throw new DAOException("can not get idGetter.");

		Object idVal;
		try {
			idVal = idGetter.invoke(this.t);
			this.fromVal = idVal == null ? null : String.valueOf(idVal);
		} catch (Exception e) {
			throw new DAOException(idGetter + " invoke exception "
					+ ExceptionInfoUtil.toString(e));
		}
	}

	/**
	 * 多对多级联插入 1.取得主对象idVal（如果没有，则先插入数据库，获取idVal）
	 * 2.取得关联对象tarIdVal（如果没有，先插入数据库，获取）3.检查下是否有重复记录 4.插入到关系表
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void insert() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			throw new DAOException(
					"there is no fields to be insert with cascade.");

		// "insert into {relTable}({from},{to}) values({fromVal},{toVal})"
		// 插入关系表
		String format = "INSERT INTO %s(%s,%s) VALUES(?,?) ";
		if (fromVal == null || "0".equals(fromVal) || "".equals(fromVal))
			throw new DAOException(
					"this obj's idVal must be valid. not is [\"null\"|\"0\"|\"\"]");

		else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null)
			throw new DAOException(
					"this obj's idVal must be recored in dataBase.");

		for (Field f : fields) {
			ManyMany ann = f.getAnnotation(ManyMany.class);
			if (ann == null)
				continue;
			Class<?> tarClass = ann.target();
			String relTable = ann.relTable();
			String tarIdField = ORMConfigBeanUtil.getIdField(tarClass);
			Method tarGetter = ru.getGetter(f.getName());
			Object _tarObj = null;
			try {
				_tarObj = tarGetter.invoke(t);
			} catch (Exception e) {
				throw new DAOException(tarGetter + " invoke exception "
						+ ExceptionInfoUtil.toString(e));
			}
			// 检查一下目标对象是否存在于数据库
			if (_tarObj == null)
				continue;

			List<?> tarList = (List<?>) _tarObj;
			for (int i = 0; i < tarList.size(); i++) {
				Object tarObj = tarList.get(i);
				String from = ann.from();
				String to = ann.to();

				ReflectUtil tarRu = new ReflectUtil(tarObj);
				Method tarIdGetter = tarRu.getGetter(tarIdField);
				Object _tarIdVal = null;

				try {
					_tarIdVal = tarIdGetter.invoke(tarObj);
				} catch (Exception e) {
					throw new DAOException(tarIdGetter + " invoke exception "
							+ ExceptionInfoUtil.toString(e));
				}

				if (_tarIdVal == null)
					continue;

				String tarIdVal = String.valueOf(_tarIdVal);
				Object tempObj = DAOFactory.getSelectDAO(dsName).selectOne(
						tarClass, new String[] { tarIdField },
						new String[] { tarIdVal });

				if (tempObj == null) {
					// 如果目标对象不存在于数据库，则将目标对象插入到数据库
					Object tarIdValObj = DAOFactory.getInsertDAO(dsName)
							.insert(tarObj);
					// 将获取到的id值注入到tarObj中
					Method tarIdSetter = tarRu.getSetter(tarIdField);
					try {
						tarIdSetter.invoke(tarObj, tarIdValObj);
					} catch (Exception e) {
						throw new DAOException(tarIdSetter
								+ " invoke exception " + ExceptionInfoUtil.toString(e));
					}
					tarIdVal = String.valueOf(tarIdValObj);
				}

				// 插入到关系表中
				// 先检查下是否有重复记录
				// "select * from {relTable} where {from} = {fromVal} and {to} = {toVal} "
				String _format = "select * from %s where %s = ? and %s = ? ";
				String _sql = String.format(_format, relTable, from, to);
				if (DAOFactory.getSelectDAO(dsName).selectBySQL(Map.class,
						_sql, fromVal, tarIdVal) != null)
					continue;

				// "INSERT INTO %s(%s,%s) VALUES(?,?) "
				String sql = String.format(format, relTable, from, to);
				DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(sql,
						fromVal, tarIdVal);
			}
		}
	}

	/**
	 * 多对多级联删除 1.如果主对象不存在与数据库，不处理 2.否则，检查当前主对象中的关联对象，如果关联对象为空，则删除所有与主对象有关的关联关系。
	 * 3.如果当前主对象中含有关联对象，则删除这些关联对象与主对象的关系
	 * 
	 * 
	 */
	public void delete() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;
		
		// "delete from {relTable} WHERE {from} = {fromVal} ;"
		String format = "delete from %s WHERE %s = ? ";
		if (fromVal == null || "0".equals(fromVal) || "".equals(fromVal))
			return;
		else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null)
			return;

		for (Field f : fields) {
			ManyMany ann = f.getAnnotation(ManyMany.class);
			if (ann == null)
				continue;

			String relTable = ann.relTable();
			String from = ann.from();

			Method tarGetter = ru.getGetter(f.getName());

			List<?> tarList = null;

			try {
				tarList = (List<?>) tarGetter.invoke(t);
			} catch (Exception e) {
				throw new DAOException(tarGetter + " invoke exception "
						+ ExceptionInfoUtil.toString(e));
			}

			if (tarList == null || tarList.size() == 0) {
				String sql = String.format(format, relTable, from);
				// 删除所有关联记录
				DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(sql,
						fromVal);
			} else {
				// 删除指定关联的记录
				String to = ann.to();
				Class<?> tarClass = ann.target();
				// "delete from {relTable} where {from} = {fromVal} and to = {toVal}"
				String _format = "delete from %s where %s = ? and %s = ?";
				for (int i = 0; i < tarList.size(); i++) {
					Object tarObj = tarList.get(i);
					if (tarObj == null)
						continue;
					ReflectUtil tarRu = new ReflectUtil(tarObj);
					String tarIdField = ORMConfigBeanUtil.getIdField(tarClass);
					Method tarIdGetter = tarRu.getGetter(tarIdField);
					Object toValObj = null;

					try {
						toValObj = tarIdGetter.invoke(tarObj);
					} catch (Exception e) {
						throw new DAOException(tarIdGetter
								+ "invoke exception " + ExceptionInfoUtil.toString(e));
					}

					if (toValObj == null)
						continue;
					
					String toVal = String.valueOf(toValObj);
					if (DAOFactory.getSelectDAO(dsName)
							.selectOne(tarClass, new String[] { tarIdField },
									new String[] { toVal }) == null)
						continue;
					
					String _sql = String.format(_format, relTable, from, to);
					DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(_sql,
							fromVal, toVal);
				}
			}
		}
	}

//	/**
//	 * 多对多级联更新
//	 * 1.前提是主对象必须存在于数据库，否则不处理
//	 * 2.将要更新的关联对象也必须要存在于数据库，否则不处理
//	 * 4.关联对象如果在关系表中找不到，则不会更新关联对象，否则更新
//	 * 
//	 * @throws DAOException
//	 */
//	public void update() throws DAOException {
//		if (this.fields == null || this.fields.size() == 0)
//			return;
//		if (fromVal == null || "0".equals(fromVal) || "".equals(fromVal))
//			return;
//		else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null)
//			return;
//		for (Field f : fields) {
//			ManyMany ann = f.getAnnotation(ManyMany.class);
//			if (ann == null)
//				continue;
//
//			Method tarGetter = ru.getGetter(f.getName());
//			if (tarGetter == null)
//				continue;
//			try {
//				List<?> tarList = (List<?>) tarGetter.invoke(t);
//				if (tarList != null) {
//					String relTable = ann.relTable();
//					String to = ann.to();
//					String from = ann.from();
//
//					for (int i = 0; i < tarList.size(); i++) {
//						Object tarObj = tarList.get(i);
//						String tarIdField = ORMConfigBeanUtil.getIdField(tarObj
//								.getClass());
//						ReflectUtil tarRu = new ReflectUtil(tarObj);
//						Method tarIdGetter = tarRu.getGetter(tarIdField);
//						Object toValObj = tarIdGetter.invoke(tarObj);
//						if (toValObj == null)
//							continue;
//
//						// "select * from {relTable} where {from} = {fromVal} and {to} = {toVal} "
//						String _format = "select * from %s where %s = ? and %s = ? ";
//						String _sql = String
//								.format(_format, relTable, from, to);
//						if (DAOFactory.getSelectDAO(dsName).selectBySQL(
//								Map.class, _sql, fromVal,
//								String.valueOf(toValObj)) == null)
//							continue;
//
//						// 更新关联对象
//						DAOFactory.getUpdateDAO(dsName).update(tarObj);
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new DAOException("" + ExceptionInfoUtil.toString(e));
//			}
//		}
//	}

	/**
	 * 多对多级联查询 1.当主对象没有包含任何一个关联对象时，默认查询所有与之关联的对象
	 * 2.当主对象中包含了关联对象时（含有其id值），则只查询这些关联的对象
	 * 
	 * @param <T>
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void select() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;
		// select * from {tarTable} where {tarIdColumn} in (select {to} from
		// {relTable} where {from} = {fromVal})
		String format = "SELECT * FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s = ?) ";
		if (fromVal == null || "0".equals(fromVal) || "".equals(fromVal))
			return;
		// else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField)
		// == null)
		// return;

		for (Field f : fields) {
			ManyMany ann = f.getAnnotation(ManyMany.class);
			if (ann == null)
				continue;
			// 多对多关系目标Class
			Class<?> tarClass = ann.target();
			String tarTable = ORMConfigBeanUtil.getTable(tarClass);
			// 目标类对应的数据库表Id字段
			String tarIdColumn = ORMConfigBeanUtil.getIdColumn(tarClass);
			// 目标类在第三方关系表中的字段名
			String to = ann.to();
			// 第三方关系表
			String relTable = ann.relTable();
			// 主类在第三方关系表中的字段名
			String from = ann.from();

			Method tarGetter = ru.getGetter(f.getName());
			try {
				List<?> tarList = null;
				tarList = (List<?>) tarGetter.invoke(t);

				if (tarList != null && tarList.size() > 0) {
					for (int i = 0; i < tarList.size(); i++) {
						Object tarObj = tarList.get(i);
						ReflectUtil tarRu = new ReflectUtil(tarObj);
						String tarIdField = ORMConfigBeanUtil
								.getIdField(tarClass);
						Method tarIdGetter = tarRu.getGetter(tarIdField);
						Object tarIdValObj = tarIdGetter.invoke(tarObj);
						if (tarIdValObj == null)
							continue;
						String tarIdVal = String.valueOf(tarIdValObj);
						// 查询 select * from {tarTable} where {tarIdColumn} =
						// {tarIdVal}
						tarObj = DAOFactory.getSelectDAO(dsName).selectOne(
								tarClass, new String[] { tarIdField },
								new String[] { tarIdVal });
					}
				} else {
					String sql = String.format(format, tarTable, tarIdColumn,
							to, relTable, from);
					// 从数据库中取出与当前主对象id关联的所有目标对象，
					tarList = DAOFactory.getSelectDAO(dsName).selectBySQL(
							tarClass, sql, fromVal);
				}

				// 并注入到当前主对象的属性中
				Method tarSetter = ru.getSetter(f.getName());

				tarSetter.invoke(t, tarList);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
	}
}
