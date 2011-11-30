package com.cfuture08.eweb4j.orm.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.cfuture08.eweb4j.cache.DBInfoConfigBeanCache;
import com.cfuture08.eweb4j.orm.jdbc.transaction.ConThreadLocal;
import com.cfuture08.eweb4j.orm.dao.config.DAOConfigConstant;
import com.mchange.v2.c3p0.DataSources;

/**
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public final class DataSourceWrap implements DataSource {
	private String dsName;
	private DataSource ds;

	private void init(String dsName, DataSource ds) {
		if (dsName == null) {
			this.dsName = DAOConfigConstant.MYDBINFO;
		}

		this.dsName = dsName;
		this.ds = ds;
	}

	public DataSourceWrap() {
	}

	public DataSourceWrap(String dsName, DataSource ds) {
		init(dsName, ds);
	}

	public DataSourceWrap(DataSource ds) {
		init(null, ds);
	}

	@Override
	public Connection getConnection() {
		try {
			Connection con = null;
			if (ConThreadLocal.isTrans()) {
				// 首先从当前线程获取数据库连接
				con = ConThreadLocal.getCon(dsName);
				if (con == null) {
					// 如果没有，就从连接池取出来一条
					con = ds.getConnection();
					// 然后放入到本地线程变量中保存
					ConThreadLocal.put(dsName, con);
				}
			} else {
				con = ds.getConnection();
			}

			return con;
		} catch (SQLException e) {
//			try {
//				this.finalize();
//			} catch (Throwable e1) {
//				e1.printStackTrace();
//			}
		}

		return null;
	}

	protected void finalize() throws Throwable {
		DataSourceCache.remove(this.dsName);
		DataSources.destroy(this.ds); // 关闭datasource
		DataSource _ds = DataSourceCreator.getDataSource(DBInfoConfigBeanCache
				.get(dsName));
		DataSourceWrap dsw = new DataSourceWrap(dsName, _ds);
		DataSourceCache.put(dsName, dsw);
		super.finalize();

	}

	public String getDsName() {
		return dsName;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
