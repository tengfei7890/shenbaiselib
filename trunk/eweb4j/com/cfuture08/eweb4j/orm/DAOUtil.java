package com.cfuture08.eweb4j.orm;

import java.sql.Connection;
import java.sql.SQLException;

import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.eweb4j.orm.jdbc.JdbcUtil;

public class DAOUtil {
	public static Number selectMaxId(Class<?> clazz, Connection con)
			throws SQLException {
		String idColumn = ORMConfigBeanUtil.getIdColumn(clazz);
		String table = ORMConfigBeanUtil.getTable(clazz);
		String format = "select max(%s) from %s ";
		String sql = String.format(format, idColumn, table);
		Number id = JdbcUtil.getInteger(con, sql);// 获得最新插入的ID值
		return id;
	}
}
