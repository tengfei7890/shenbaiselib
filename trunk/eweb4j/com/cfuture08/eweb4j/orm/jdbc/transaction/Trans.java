package com.cfuture08.eweb4j.orm.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import com.cfuture08.eweb4j.orm.jdbc.DataSourceCache;

public abstract class Trans {
	private List<Connection> cons = new ArrayList<Connection>();
	public void begin() throws SQLException {

		ConThreadLocal.lock(true);
		Set<Entry<String, DataSource>> set = DataSourceCache.entrySet();
		for (Entry<String, DataSource> e : set) {
			Connection con = e.getValue().getConnection();
			con.setAutoCommit(false);
			cons.add(con);
		}
	}

	public void commit() throws SQLException {
		if (cons != null)
			for (Connection con : cons) {
				con.commit();
			}
	}

	public void rollback() throws SQLException {
		if (cons != null)
			for (Connection con : cons) {
				con.rollback();
			}
	}

	public void close() throws SQLException {
		ConThreadLocal.lock(false);
		if (cons != null) {
			for (Connection con : cons) {
				con.close();
			}
		}
		ConThreadLocal.remove();// 移除当前线程中已经被关闭的连接
	}

	public abstract void run(Object... args) throws Exception;
}
