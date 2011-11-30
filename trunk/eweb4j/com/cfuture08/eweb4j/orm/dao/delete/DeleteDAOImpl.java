package com.cfuture08.eweb4j.orm.dao.delete;

import java.sql.Connection;

import javax.sql.DataSource;

import com.cfuture08.eweb4j.orm.DAOUtil;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.jdbc.JdbcUtil;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;
import com.cfuture08.util.ExceptionInfoUtil;

public class DeleteDAOImpl implements DeleteDAO {
	private DataSource ds;

	public DeleteDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public <T> Number[] batchDelete(T... ts) throws DAOException {
		Number[] ids = null;
		Connection con = null;
		if (ts == null || ts.length == 0)
			return ids;

		ids = new Number[ts.length];
		try {
			con = ds.getConnection();
			for (int i = 0; i < ts.length; i++) {
				String[] sqls = SqlFactory.getDeleteSql(new Object[] { ts[i] })
						.delete();
				if (sqls == null)
					ids[i] = -1;
				else {
					int rs = (Integer) JdbcUtil.update(con, sqls[0]);
					if (rs > 0) {
						Class<?> clazz = ts[i].getClass();
						ids[i] = DAOUtil.selectMaxId(clazz, ds.getConnection());
						// 缓存
					}
				}
			}
		} catch (Exception e) {
			throw new DAOException("" + ExceptionInfoUtil.toString(e));
		}

		return ids;
	}

	@Override
	public <T> Number delete(T t) throws DAOException {
		Number[] rs = batchDelete(new Object[] { t });
		return rs == null ? 0 : rs[0];
	}

	@Override
	public <T> Number[] deleteByFieldIsValue(Class<T>[] clazz, String[] fields,
			String[] values) throws DAOException {
		Number[] ids = null;
		Connection con = null;
		if (clazz == null || clazz.length == 0)
			return ids;

		ids = new Number[clazz.length];
		try {
			con = ds.getConnection();
			Object[] ts = new Object[clazz.length];
			for (int i = 0; i < clazz.length; ++i) {
				ts[i] = clazz[i].newInstance();
			}

			for (int i = 0; i < ts.length; i++) {
				String[] sqls = SqlFactory.getDeleteSql(ts[i]).delete(fields,
						values);
				if (sqls == null)
					ids[i] = -1;
				else {
					int rs = (Integer) JdbcUtil.update(con, sqls[0]);
					if (rs > 0) {
						Class<?> cls = ts[i].getClass();
						ids[i] = DAOUtil.selectMaxId(cls, ds.getConnection());
						// 缓存
					}
				}
			}
		} catch (Exception e) {
			throw new DAOException("" + ExceptionInfoUtil.toString(e));
		}

		return ids;
	}

	@Override
	public <T> Number[] deleteByFieldIsValue(Class<T>[] clazz, String field,
			String value) throws DAOException {
		return this.deleteByFieldIsValue(clazz, new String[] { field },
				new String[] { value });
	}

	@Override
	public <T> Number deleteByFields(T t, String[] fields) throws DAOException {
		Number id = 0;
		if (t != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				String[] sqls = SqlFactory.getDeleteSql(new Object[] { t })
						.delete(fields);
				id = JdbcUtil.update(con, sqls[0]);
				Class<?> cls = t.getClass();
				id = DAOUtil.selectMaxId(cls, ds.getConnection());
				// 缓存
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
		return id;
	}

	@Override
	public <T> Number deleteByFieldIsValue(Class<T> clazz, String[] fields,
			String[] values) throws DAOException {
		Number id = 0;
		if (clazz != null && fields != null && values != null
				&& fields.length == values.length) {
			Connection con = null;
			try {
				con = ds.getConnection();
				String[] sqls = SqlFactory.getDeleteSql(
						new Object[] { clazz.newInstance() }).delete(fields,
						values);
				id = JdbcUtil.update(con, sqls[0]);
				id = DAOUtil.selectMaxId(clazz, ds.getConnection());
				// 缓存
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
		return id;
	}

	@Override
	public <T> Number deleteByFieldIsValue(Class<T> clazz, String field,
			String value) throws DAOException {
		return this.deleteByFieldIsValue(clazz, new String[] { field },
				new String[] { value });
	}

	@Override
	public <T> Number deleteWhere(Class<T> clazz, String condition,
			Object[] args) throws DAOException {
		Number id = 0;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getDeleteSql(
						new Object[] { clazz.newInstance() }).deleteWhere(
						condition);
				id = JdbcUtil.updateWithArgs(con, sql, args);
				//缓存
			} catch (Exception e) {
				throw new DAOException("" + ExceptionInfoUtil.toString(e));
			}
		}
		return id;
	}
}
