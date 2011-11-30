package com.cfuture08.eweb4j.orm.dao.insert;

import java.sql.Connection;

import javax.sql.DataSource;

import com.cfuture08.eweb4j.orm.DAOUtil;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.jdbc.JdbcUtil;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;
import com.cfuture08.util.ExceptionInfoUtil;

public class InsertDAOImpl implements InsertDAO {
	private DataSource ds;

	public InsertDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public <T> Number[] batchInsert(T... ts) throws DAOException {
		Number[] ids = null;
		Connection con = null;
		if (ts == null || ts.length == 0)
			return ids;

		ids = new Number[ts.length];

		try {
			con = ds.getConnection();
			for (int i = 0; i < ts.length; i++) {
				String[] sqls = SqlFactory.getInsertSql(new Object[] { ts[i] })
						.create();
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
			e.printStackTrace();
			throw new DAOException("" + ExceptionInfoUtil.toString(e));
		}

		return ids;
	}

	@Override
	public <T> Number insert(T t) throws DAOException {
		Number[] rs = batchInsert(new Object[] { t });
		return rs == null ? 0 : rs[0];
	}

	@Override
	public <T> Number[] insertByCondition(T[] ts, String condition)
			throws DAOException {
		Number[] ids = null;
		Connection con = null;
		if (ts == null || ts.length == 0)
			return ids;

		ids = new Number[ts.length];
		try {
			con = ds.getConnection();
			for (int i = 0; i < ts.length; i++) {
				String[] sqls = SqlFactory.getInsertSql(new Object[] { ts[i] })
						.create(condition);
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
	public <T> Number[] insertByFields(T[] ts, String[][] fields)
			throws DAOException {
		Number[] ids = null;
		Connection con = null;
		if (ts == null || ts.length == 0)
			return ids;

		ids = new Number[ts.length];
		try {
			con = ds.getConnection();

			for (int i = 0; i < ts.length; i++) {
				String[] sqls = SqlFactory.getInsertSql(new Object[] { ts[i] })
						.createByFields(fields);
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
	public <T> Number insertByField(T t, String[] field) throws DAOException {
		Number[] rs = this.insertByFields(new Object[] { t },
				new String[][] { field });
		return rs == null ? 0 : rs[0];
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
}
