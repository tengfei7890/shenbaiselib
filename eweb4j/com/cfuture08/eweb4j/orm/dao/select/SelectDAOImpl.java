package com.cfuture08.eweb4j.orm.dao.select;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.jdbc.JdbcUtil;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;
import com.cfuture08.eweb4j.orm.sql.constant.OrderType;
import com.cfuture08.util.ExceptionInfoUtil;
import com.cfuture08.util.StringUtil;

public class SelectDAOImpl implements SelectDAO {
	private DataSource ds;
	private String dbType;

	public SelectDAOImpl(DataSource ds, String dbType) {
		this.ds = ds;
		this.dbType = dbType;
	}

	@Override
	public <T> List<T> selectAll(Class<T> clazz, String orderField,
			int orderType) throws DAOException {
		List<T> list = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(clazz.newInstance(),
						dbType).selectAll(orderField, orderType);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> selectAll(Class<T> clazz, int orderType)
			throws DAOException {
		return this.selectAll(clazz, null, orderType);
	}

	@Override
	public <T> List<T> selectAll(Class<T> clazz) throws DAOException {
		return this.selectAll(clazz, null, OrderType.DESC_ORDER);
	}

	@Override
	public <T> T selectOne(T t, String... fields) throws DAOException {
		T result = null;
		if (t != null) {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields);
				List<T> list = JdbcUtil.getList(con, clazz, sql);
				if (list != null && !list.isEmpty()) {
					result = list.get(0);
				}
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
		return result;
	}

	@Override
	public <T> T selectOne(Class<T> clazz, String[] fields, String[] values)
			throws DAOException {
		T result = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				T t = clazz.newInstance();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, values);
				List<T> list = JdbcUtil.getList(con, clazz, sql);
				if (list != null && !list.isEmpty()) {
					result = list.get(0);
				}
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return result;
	}

	@Override
	public <T> T selectOneByWhere(Class<T> clazz, String condition)
			throws DAOException {
		T result = null;
		List<T> list = this.selectWhere(clazz, condition);
		if (list != null && !list.isEmpty()) {
			result = list.get(0);
		}
		return result;
	}

	@Override
	public <T> long selectCount(Class<T> clazz) throws DAOException {
		return this.selectCount(clazz, null);
	}

	@Override
	public <T> long selectCount(Class<T> clazz, String condition)
			throws DAOException {
		long result = -1;
		Connection con = null;
		try {
			con = ds.getConnection();
			if (clazz != null) {
				T t = clazz.newInstance();
				String sql = SqlFactory.getSelectSql(t, dbType).selectCount(
						condition);
				String str = String.valueOf(JdbcUtil.getObject(
						con, sql));
				if (StringUtil.isNumeric(str)) {
					result = Integer.parseInt(str);
				}
			}
		} catch (Exception e) {
			throw new DAOException("" + ExceptionInfoUtil.toString(e));
		} 
		return result;
	}

	@Override
	public <T> List<T> selectWhere(Class<T> clazz, String condition,Object... args)
			throws DAOException {
		List<T> result = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			T t = clazz.newInstance();
			String sql = SqlFactory.getSelectSql(t, dbType).select(condition);
			result = JdbcUtil.getListWithArgs(con, clazz, sql,args);
		} catch (Exception e) {
			throw new DAOException("" + ExceptionInfoUtil.toString(e));
		} 
		return result;
	}

	@Override
	public <T> List<T> selectBySQL(Class<T> clazz, String sql, Object... args)
			throws DAOException {
		List<T> result = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			result = JdbcUtil.getListWithArgs(con, clazz, sql,
					args);
		} catch (Exception e) {
			throw new DAOException("" + ExceptionInfoUtil.toString(e));
		} 

		return result;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public <T> long selectCount(Class<T> clazz, String condition,
			Object... args) throws DAOException {
		long result = -1;
		Connection con = null;
		try {
			con = ds.getConnection();
			if (clazz != null) {
				T t = clazz.newInstance();
				String sql = SqlFactory.getSelectSql(t, dbType).selectCount(
						condition);
				String str = String.valueOf(JdbcUtil.getObject(
						con, sql,args));
				if (StringUtil.isNumeric(str)) {
					result = Integer.parseInt(str);
				}
			}
		} catch (Exception e) {
			throw new DAOException("" + ExceptionInfoUtil.toString(e));
		} 
		return result;
	}
}
