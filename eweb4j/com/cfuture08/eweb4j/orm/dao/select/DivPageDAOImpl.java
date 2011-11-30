package com.cfuture08.eweb4j.orm.dao.select;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.jdbc.JdbcUtil;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;
import com.cfuture08.util.ExceptionInfoUtil;

public class DivPageDAOImpl implements DivPageDAO {
	private DataSource ds;
	private String dbType;

	public DivPageDAOImpl(DataSource ds, String dbType) {
		this.ds = ds;
		this.dbType = dbType;
	}

	@Override
	public <T> T nextOne(T t, int orderType) throws DAOException {
		if (t != null) {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			Connection con = null;
			try {
				con = ds.getConnection();
				List<T> list = JdbcUtil.getList(con, clazz,
						SqlFactory.getSelectSql(t, dbType).nextOne(orderType));
				return list != null && !list.isEmpty() ? list.get(0) : null;
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
		return null;
	}

	@Override
	public <T> T nextOne(Class<T> clazz, String column, String value,
			int orderType) throws DAOException {
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				List<T> list = JdbcUtil.getList(con, clazz,
						SqlFactory.getSelectSql(clazz.newInstance(), dbType)
								.nextOne(column, value, orderType));
				return list != null && !list.isEmpty() ? list.get(0) : null;
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
		return null;
	}

	@Override
	public <T> T preOne(T t, int orderType) throws DAOException {
		if (t != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				@SuppressWarnings("unchecked")
				List<T> list = JdbcUtil.getList(con,
						(Class<T>) t.getClass(),
						SqlFactory.getSelectSql(t, dbType).preOne(orderType));
				return list != null && !list.isEmpty() ? list.get(0) : null;
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return null;
	}

	@Override
	public <T> T preOne(Class<T> clazz, String column, String value,
			int orderType) throws DAOException {
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				List<T> list = JdbcUtil.getList(con, clazz,
						SqlFactory.getSelectSql(clazz.newInstance(), dbType)
								.preOne(column, value, orderType));
				return list != null && !list.isEmpty() ? list.get(0) : null;
			} catch (Exception e) {

				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return null;
	}

	@Override
	public <T> List<T> divPageByWhere(Class<T> clazz, int currPage,
			int numPerPage, String orderField, int orderType, String condition,
			Object[] args) throws DAOException {
		List<T> list = null;
		Connection con = null;
		if (clazz != null) {
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(clazz.newInstance(),
						dbType).divPage(currPage, numPerPage, orderField,
						orderType, condition);
				list = JdbcUtil.getListWithArgs(con, clazz, sql,
						args);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPage(Class<T> clazz, int currPage, int numPerPage,
			String orderField, int orderType) throws DAOException {
		return this.divPageByWhere(clazz, currPage, numPerPage, orderField,
				orderType, null, null);
	}

	@Override
	public <T> List<T> divPage(Class<T> clazz, int currPage, int numPerPage,
			int orderType) throws DAOException {
		return this.divPageByWhere(clazz, currPage, numPerPage, null,
				orderType, null, null);
	}

	@Override
	public <T> List<T> divPageByWhere(Class<T> clazz, int currPage,
			int numPerPage, int orderType, String condition, Object[] args)
			throws DAOException {
		return this.divPageByWhere(clazz, currPage, numPerPage, null,
				orderType, condition, args);
	}

	@Override
	public <T> List<T> divPage(Class<T> clazz, int currPage, int numPerPage)
			throws DAOException {
		return this.divPageByWhere(clazz, currPage, numPerPage, null, -1, null,
				null);
	}

	@Override
	public <T> List<T> divPageByWhere(Class<T> clazz, int currPage,
			int numPerPage, String condition, Object[] args)
			throws DAOException {
		return this.divPageByWhere(clazz, currPage, numPerPage, null, -1, condition,
				args);
	}

	@Override
	public <T> List<T> divPageByFieldIsValue(Class<T> clazz, String[] fields,
			String[] values, String orderField, int orderType, int currPage,
			int numPerPage, boolean isOR) throws DAOException {
		List<T> list = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(clazz.newInstance(),
						dbType).selectWhere(fields, values, -2, false, false,
						isOR, orderField, orderType, currPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldNotIsValue(Class<T> clazz,
			String[] fields, String[] values, String orderField, int orderType,
			int currPage, int numPerPage, boolean isOR) throws DAOException {
		List<T> list = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(clazz.newInstance(),
						dbType).selectWhere(fields, values, -2, false, true,
						isOR, orderField, orderType, currPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldIsValue(T t, String[] fields,
			String orderField, int orderType, int currPage, int numPerPage,
			boolean isOR) throws DAOException {
		List<T> list = null;
		if (t != null) {
			Connection con = null;
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, null, -2, false, false, isOR, orderField,
						orderType, currPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldNotIsValue(T t, String[] fields,
			String orderField, int orderType, int currPage, int numPerPage,
			boolean isOR) throws DAOException {
		List<T> list = null;
		if (t != null) {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, null, -2, false, true, isOR, orderField,
						orderType, currPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldIsValue(Class<T> clazz, String[] fields,
			String[] values, int orderType, int currPage, int numPerPage,
			boolean isOR) throws DAOException {
		return this.divPageByFieldIsValue(clazz, fields, values, null,
				orderType, currPage, numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldNotIsValue(Class<T> clazz,
			String[] fields, String[] values, int orderType, int currPage,
			int numPerPage, boolean isOR) throws DAOException {
		return this.divPageByFieldNotIsValue(clazz, fields, values, null,
				orderType, currPage, numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldIsValues(T t, String[] fields,
			int orderType, int currPage, int numPerPage, boolean isOR)
			throws DAOException {
		List<T> list = null;
		if (t != null) {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, null, -2, false, false, isOR, null, orderType,
						currPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldNotIsValues(T t, String[] fields,
			int orderType, int currPage, int numPerPage, boolean isOR)
			throws DAOException {
		List<T> list = null;
		if (t != null) {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, null, -2, false, true, isOR, null, orderType,
						currPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldIsValue(Class<T> clazz, String[] fields,
			String[] values, int currPage, int numPerPage, boolean isOR)
			throws DAOException {
		return this.divPageByFieldIsValue(clazz, fields, values, null, -1,
				currPage, numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldNotIsValue(Class<T> clazz,
			String[] fields, String[] values, int currPage, int numPerPage,
			boolean isOR) throws DAOException {
		return this.divPageByFieldNotIsValue(clazz, fields, values, null, -1,
				currPage, numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldIsValue(T t, String[] fields,
			int currPage, int numPerPage, boolean isOR) throws DAOException {
		return this.divPageByFieldIsValue(t, fields, null, -1, currPage,
				numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldNotIsValue(T t, String[] fields,
			int currPage, int numPerPage, boolean isOR) throws DAOException {
		return this.divPageByFieldNotIsValue(t, fields, null, -1, currPage,
				numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldLikeValue(Class<T> clazz, String[] fields,
			String[] values, int likeType, String orderField, int orderType,
			int currPage, int numPerPage, boolean isOR) throws DAOException {
		List<T> list = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(clazz.newInstance(),
						dbType).selectWhere(fields, values, likeType, true,
						false, isOR, orderField, orderType, currPage,
						numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldNotLikeValue(Class<T> clazz,
			String[] fields, String[] values, int likeType, String orderField,
			int orderType, int currPage, int numPerPage, boolean isOR)
			throws DAOException {
		List<T> list = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(clazz.newInstance(),
						dbType)
						.selectWhere(fields, values, likeType, true, true,
								isOR, orderField, orderType, currPage,
								numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {

				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldLikeValue(T t, String[] fields,
			int likeType, String orderField, int orderType, int currPage,
			int numPerPage, boolean isOR) throws DAOException {
		List<T> list = null;
		if (t != null) {
			Connection con = null;
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, null, likeType, true, false, isOR, orderField,
						orderType, currPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldNotLikeValue(T t, String[] fields,
			int likeType, String orderField, int orderType, int currPage,
			int numPerPage, boolean isOR) throws DAOException {
		List<T> list = null;
		if (t != null) {
			Connection con = null;
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, null, likeType, true, true, isOR, orderField,
						orderType, currPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			} 
		}
		return list;
	}

	@Override
	public <T> List<T> divPageByFieldLikeValue(Class<T> clazz, String[] fields,
			String[] values, int likeType, int orderType, int currPage,
			int numPerPage, boolean isOR) throws DAOException {
		return this.divPageByFieldLikeValue(clazz, fields, values, likeType,
				null, orderType, currPage, numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldNotLikeValue(Class<T> clazz,
			String[] fields, String[] values, int likeType, int orderType,
			int currPage, int numPerPage, boolean isOR) throws DAOException {
		return this.divPageByFieldNotLikeValue(clazz, fields, values, likeType,
				null, orderType, currPage, numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldLikeValue(T t, String[] fields,
			int likeType, int orderType, int currPage, int numPerPage,
			boolean isOR) throws DAOException {
		return this.divPageByFieldLikeValue(t, fields, likeType, null,
				orderType, currPage, numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldNotLikeValue(T t, String[] fields,
			int likeType, int orderType, int currPage, int numPerPage,
			boolean isOR) throws DAOException {
		return this.divPageByFieldNotLikeValue(t, fields, likeType, null,
				orderType, currPage, numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldLikeValue(Class<T> clazz, String[] fields,
			String[] values, int likeType, int currPage, int numPerPage,
			boolean isOR) throws DAOException {
		return this.divPageByFieldLikeValue(clazz, fields, values, likeType,
				-1, currPage, numPerPage, isOR);
	}

	@Override
	public <T> List<T> divPageByFieldNotLikeValue(Class<T> clazz,
			String[] fields, String[] values, int likeType, int currPage,
			int numPerPage, boolean isOR) throws DAOException {
		return this.divPageByFieldNotLikeValue(clazz, fields, values, likeType,
				-1, currPage, numPerPage, isOR);
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
}
