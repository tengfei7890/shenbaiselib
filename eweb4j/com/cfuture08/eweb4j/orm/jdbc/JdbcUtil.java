package com.cfuture08.eweb4j.orm.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.orm.RowMapping;
import com.cfuture08.eweb4j.orm.jdbc.transaction.ConThreadLocal;
import com.cfuture08.util.ExceptionInfoUtil;

public class JdbcUtil {

	/**
	 * 执行更新sql语句
	 * 
	 * @param con
	 * @param sqls
	 * @return
	 * @throws Exception
	 */
	public static Number[] update(Connection con, String[] sqls)
			throws JdbcUtilException {
		return updateWithArgs(con, sqls, null);
	}

	public static Number update(Connection con, String sql) {
		return updateWithArgs(con, sql, null);
	}

	/**
	 * 执行sql语句，更新操作，支持有？占位符
	 * 
	 * @param con
	 * @param sqls
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static Number[] updateWithArgs(Connection con, String[] sqls,
			Object[][] args) throws JdbcUtilException {
		Number[] result = null;

		if (con != null && sqls != null && sqls.length > 0) {
			StringBuilder sql = new StringBuilder();
			PreparedStatement pstmt = null;
			try {
				if (args != null && args.length > 0)
					if (args.length != sqls.length)
						throw new JdbcUtilException("args error ...");

				result = new Number[sqls.length];
				for (int i = 0; i < sqls.length; ++i) {
					sql.append(sqls[i]);
					pstmt = con.prepareStatement(sqls[i]);
					fillArgs(args, pstmt, i);
					result[i] = pstmt.executeUpdate();
					pstmt.close();
					logToOrm(sqls, result, i);
				}
			} catch (SQLException e) {

				logException(sql, e);
				throw new JdbcUtilException(sql + " exception cause by:"
						+ ExceptionInfoUtil.toString(e));
			} finally {
				close(null, pstmt, null);
				if (!ConThreadLocal.isTrans()) {
					close(con);
				}
			}
		}

		return result;
	}

	/**
	 * 填充参数
	 * 
	 * @param args
	 * @param pstmt
	 * @param i
	 * @throws SQLException
	 */
	private static void fillArgs(Object[][] args, PreparedStatement pstmt, int i)
			throws SQLException {
		if (args != null && args.length > 0)
			if (args[i] != null && args[i].length > 0)
				for (int j = 0; j < args[i].length; ++j)
					pstmt.setObject(j + 1, args[i][j]);

	}

	/**
	 * 获取List数据
	 * 
	 * @param <T>
	 * @param con
	 * @param clazz
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getList(Connection con, Class<T> clazz, String sql)
			throws JdbcUtilException {
		return getListWithArgs(con, clazz, sql, null);
	}

	/**
	 * 获取List数据，支持？占位符
	 * 
	 * @param <T>
	 * @param con
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getListWithArgs(Connection con, Class<T> clazz,
			String sql, Object[] args) throws JdbcUtilException {
		return getListWithoutCache(con, clazz, sql, args);
	}

	/**
	 * 获取List数据
	 * 
	 * @param <T>
	 * @param con
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static <T> List<T> getListWithoutCache(Connection con,
			Class<T> clazz, String sql, Object[] args) throws JdbcUtilException {

		List<T> list = null;
		if (clazz == null || sql == null) {
			return null;
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if (con != null) {
			try {
				pstmt = con.prepareStatement(sql);
				if (args != null && args.length > 0) {
					for (int i = 0; i < args.length; ++i) {
						pstmt.setString(i + 1, String.valueOf(args[i]));
					}
				}
				ResultSetMetaData rsmd = pstmt.getMetaData();
				rs = pstmt.executeQuery();
				list = RowMapping.mapRows(rs, rsmd, clazz);

				// ----------------------
				logOrm(sql, list);
			} catch (Exception e) {
				logException(sql, e);
				throw new JdbcUtilException(sql + " exception cause by:"
						+ ExceptionInfoUtil.toString(e));
			} finally {
				close(rs, pstmt, null);
				if (!ConThreadLocal.isTrans()) {
					close(con);
				}

				// MyORMCache._canCache = true;
			}
		}

		return list;
	}

	/**
	 * 更新
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static Number updateWithArgs(Connection con, String sql,
			Object[] args) throws JdbcUtilException {
		Number[] res = updateWithArgs(con, new String[] { sql },
				new Object[][] { args });
		return res == null ? 0 : res[0];
	}

	/**
	 * 获取
	 * 
	 * @param con
	 * @param sql
	 * @return
	 * @throws JdbcUtilException
	 */
	public static Object getObject(Connection con, String sql)
			throws JdbcUtilException {
		return getObject(con, sql, null);
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static String getString(Connection con, String sql, Object[] args)
			throws JdbcUtilException {
		return String.valueOf(getObject(con, sql, args));
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @return
	 * @throws JdbcUtilException
	 */
	public static String getString(Connection con, String sql)
			throws JdbcUtilException {
		return getString(con, sql, null);
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @return
	 */
	public static Integer getInteger(Connection con, String sql) {
		String temp = getString(con, sql);
		int result = temp == null ? 0 : Integer.parseInt(temp);
		return result;
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 */
	public static Integer getInteger(Connection con, String sql, Object... args) {
		String temp = getString(con, sql, args);
		int result = temp == null ? 0 : Integer.parseInt(temp);
		return result;
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	private static Map<String, Object> getResultSetAsMap(Connection con,
			String sql, Object[] args) throws JdbcUtilException {
		Map<String, Object> result = null;
		if (sql == null) {
			return null;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		if (con != null) {
			try {
				pstmt = con.prepareStatement(sql);
				if (args != null && args.length > 0) {
					for (int i = 0; i < args.length; ++i) {
						pstmt.setObject(i + 1, args[i]);
					}
				}
				rs = pstmt.executeQuery();
				rsmd = rs.getMetaData();
				List<String> columns = new ArrayList<String>();
				for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
					columns.add(rsmd.getColumnName(i));
				}
				while (rs.next()) {
					result = new HashMap<String, Object>();
					for (int i = 1; i <= columns.size(); ++i) {
						String name = columns.get(i - 1);
						result.put(name, rs.getObject(name));
					}
				}
				logOrm(sql, result);
			} catch (Exception e) {

				logException(sql, e);
				throw new JdbcUtilException(sql + " exception cause by:"
						+ ExceptionInfoUtil.toString(e));
			} finally {
				close(rs, pstmt, null);
				if (!ConThreadLocal.isTrans()) {
					close(con);
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static Object getObject(Connection con, String sql, Object[] args)
			throws JdbcUtilException {
		return getResultSetAsList(con, sql, args).get(0);
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	private static List<Object> getResultSetAsList(Connection con, String sql,
			Object[] args) throws JdbcUtilException {
		List<Object> result = null;
		Map<String, Object> map = getResultSetAsMap(con, sql, args);
		if (map != null) {
			result = new ArrayList<Object>();
			for (Entry<String, Object> entry : map.entrySet()) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	/**
	 * 关闭资源
	 * 
	 * @param rs
	 * @param pstmt
	 * @param con
	 * @throws Exception
	 */
	public static void close(ResultSet rs, PreparedStatement pstmt,
			Connection con) throws JdbcUtilException {
		try {
			if (rs != null)
				rs.close();

			if (pstmt != null)
				pstmt.close();

			if (con != null) {
				con.close();
			}

		} catch (SQLException e) {
			throw new JdbcUtilException("jdbc connection close exception..."
					+ ExceptionInfoUtil.toString(e));
		}
	}

	public static void close(Connection con) {
		close(null, null, con);
	}

	// /**
	// * 提交事务
	// *
	// * @param con
	// * @throws JdbcUtilException
	// */
	// public static void commit(Connection con) throws JdbcUtilException {
	// if (con != null) {
	// try {
	// con.commit();
	// } catch (SQLException e) {
	//
	// throw new JdbcUtilException("" + ExceptionInfoUtil.toString(e));
	// }
	// }
	// }
	//
	// /**
	// * 开启一个事务
	// * @param con
	// * @throws JdbcUtilException
	// */
	// public static void openTrans(Connection con) throws JdbcUtilException{
	// if (con != null){
	// try {
	// con.setAutoCommit(false);
	// } catch (SQLException e) {
	//
	// throw new JdbcUtilException("" + ExceptionInfoUtil.toString(e));
	// }
	// }
	// }
	//
	// public static void rollback(Connection con) throws JdbcUtilException{
	// if (con != null){
	// try {
	// con.rollback();
	// } catch (SQLException e) {
	//
	// throw new JdbcUtilException("" + ExceptionInfoUtil.toString(e));
	// }
	// }
	// }

	private static void logException(StringBuilder sql, SQLException e) {
		StringBuilder sb = new StringBuilder(e.toString());
		for (StackTraceElement ste : e.getStackTrace())
			sb.append("\n").append(ste.toString());

		sb.append(sql.append("执行出错了！").toString());
		LogFactory.getORMLogger("ERROR").write(sb.toString());
	}

	private static void logToOrm(String[] sqls, Number[] result, int i) {
		String debug = ((ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID)).getOrm().getDebug();
		StringBuilder sb = new StringBuilder();
		sb.append("ORM:执行sql：").append(sqls[i]);
		sb.append("影响行数：").append(result[i]).append(";\n");
		if ("true".equals(debug) || "1".equals(debug)) {
			System.out.print("debug--" + sb.toString());
		}
		LogFactory.getORMLogger("INFO").write(sb.toString());
	}

	private static void logException(String sql, Exception e) {
		StringBuilder sb = new StringBuilder(e.toString());
		for (StackTraceElement ste : e.getStackTrace()) {
			sb.append("\n").append(ste.toString());
		}
		sb.append(sql).append("执行出错了！");
		LogFactory.getORMLogger("ERROR").write(sb.toString());
	}

	private static <T> void logOrm(String sql, List<T> list) {
		// ----------log-------
		String debug = ((ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID)).getOrm().getDebug();
		StringBuilder sb = new StringBuilder();
		int count = list == null ? 0 : list.size();
		sb.append("ORM:执行sql：").append(sql);
		sb.append("查出结果数：").append(count).append(";\n");
		if ("true".equals(debug) || "1".equals(debug)) {
			System.out.print("debug--" + sb.toString());
		}
		LogFactory.getORMLogger("INFO").write(sb.toString());
		// ----------------
	}

	private static void logOrm(String sql, Map<String, Object> result) {
		// ----------log-------
		String debug = ((ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID)).getOrm().getDebug();

		StringBuilder sb = new StringBuilder();
		sb.append("ORM:执行sql：").append(sql);
		sb.append("查出结果数：").append(result.size()).append(";\n");
		if ("true".equals(debug) || "1".equals(debug)) {
			System.out.print("debug--" + sb.toString());
		}
		LogFactory.getORMLogger("INFO").write(sb.toString());
		// ----------------
	}

}
