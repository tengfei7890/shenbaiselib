package com.cfuture08.eweb4j.orm.sql;

import java.lang.reflect.Method;

import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.eweb4j.orm.sql.constant.DBType;
import com.cfuture08.eweb4j.orm.sql.constant.LikeType;
import com.cfuture08.eweb4j.orm.sql.constant.OrderType;
import com.cfuture08.util.ReflectUtil;

/**
 * 创建查询的SQL语句
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * @param <T>
 */
public class SelectSqlCreator<T> {
	private T t;
	private Class<?> clazz;
	private String table;
	private String idColumn;
	private String idField;
	private String dbType;

	public SelectSqlCreator(T t, String dbType) {
		this.t = t;
		this.clazz = t.getClass();
		this.table = ORMConfigBeanUtil.getTable(clazz);
		this.idColumn = ORMConfigBeanUtil.getIdColumn(clazz);
		this.idField = ORMConfigBeanUtil.getIdField(clazz);
		this.dbType = dbType;
	}

	public String select(String condition) {
		return String.format("SELECT * FROM %s WHERE %s ;", table, condition);
	}

	public String selectCount(String condition) {
		if (condition == null) {
			return String.format("SELECT COUNT(*) FROM %s ;", table);
		}
		return String.format("SELECT COUNT(*) FROM %s WHERE %s ;", table,
				condition);
	}

	/**
	 * 各种条件查询
	 * 
	 * @param fields
	 *            构成条件的属性名数组(填写的应该是对象属性名，不是数据库字段名)
	 * @param values
	 *            构成条件的字段值
	 * @param likeType
	 *            匹配类型 -1左匹配 0全匹配 1右匹配
	 * @param isLike
	 *            是否模糊查询
	 * @param orderField
	 *            对哪个字段进行排序
	 * @param oType
	 *            升序还是降序
	 * @return 字符串
	 * @param currentPage
	 *            第几页
	 * @param numPerPage
	 *            每页显示多少条
	 * @return
	 * @throws SqlCreateException
	 */
	public String selectWhere(String[] fields, String[] values, int likeType,
			boolean isLike, boolean isNot, boolean isOR, String orderField,
			int oType, int currentPage, int numPerPage)
			throws SqlCreateException {
		String[] type = null;
		String orderType = OrderType.ASC_ORDER == oType ? "ASC" : "DESC";
		if (isLike) {
			switch (likeType) {
			case LikeType.LEFT_LIKE:
				type = isNot ? new String[] { " NOT LIKE ", " OR ", " '", "%' " }
						: new String[] { " LIKE ", " OR ", " '", "%' " };
				break;
			case LikeType.RIGHT_LIKE:
				type = isNot ? new String[] { " NOT LIKE ", " OR ", " '%", "' " }
						: new String[] { " LIKE ", " OR ", " '%", "' " };
				break;
			case LikeType.ALL_LIKE:
				type = isNot ? new String[] { " NOT LIKE ", " OR ", " '%",
						"%' " }
						: new String[] { " LIKE ", " OR ", " '%", "%' " };
			}
		} else
			type = isNot ? isOR ? new String[] { " <> ", " OR ", " '", "' " }
					: new String[] { " <> ", " AND ", " '", "' " }
					: isOR ? new String[] { " = ", " OR ", " '", "' " }
							: new String[] { " = ", " AND ", " '", "' " };

		if (orderField == null)
			orderField = idColumn;
		else
			orderField = ORMConfigBeanUtil.getColumn(clazz, orderField);

		StringBuilder condition = new StringBuilder();
		String[] columns = ORMConfigBeanUtil.getColumns(clazz, fields);
		for (int i = 0; i < columns.length; ++i) {
			if (values != null)
				if (condition.length() > 0)
					condition.append(type[1]).append(columns[i]).append(" ")
							.append(type[0]).append(type[2]).append(values[i])
							.append(type[3]);
				else
					condition.append(columns[i]).append(" ").append(type[0])
							.append(type[2]).append(values[i]).append(type[3]);

			else {
				ReflectUtil ru = new ReflectUtil(t);
				Method m = ru.getGetter(fields[i]);
				if (m == null)
					continue;
				try {
					Object value = m.invoke(t);
					if (condition.length() > 0)
						condition.append(type[1]).append(columns[i])
								.append(" ").append(type[0]).append(type[2])
								.append(value).append(type[3]);
					else
						condition.append(columns[i]).append(" ")
								.append(type[0]).append(type[2]).append(value)
								.append(type[3]);
				} catch (Exception e) {
					throw new SqlCreateException(m + " invoke exception "
							+ e.toString());
				}
			}
		}

		if (currentPage > 0 && numPerPage > 0)
			return divPage(currentPage, numPerPage, orderField, oType,
					condition.toString());
		else
			return String.format("SELECT * FROM %s WHERE %s ORDER BY %s %s ;",
					table, condition.toString(), orderField, orderType);
	}

	public String selectWhere(String[] fields, String[] values, int likeType,
			boolean isLike, boolean isNot, String orderField, int oType,
			int currentPage, int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, isLike, isNot, false,
				orderField, oType, currentPage, numPerPage);
	}

	public String selectWhere(String[] fields, int likeType, boolean isLike,
			boolean isNot, boolean isOR, String orderField, int oType,
			int currentPage, int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, isLike, isNot, isOR,
				orderField, oType, currentPage, numPerPage);
	}

	public String selectWhere(String[] fields, String[] values, int likeType,
			boolean isLike, String orderField, int oType, int currentPage,
			int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, isLike, false,
				orderField, oType, currentPage, numPerPage);
	}

	public String selectWhere(String[] fields, String[] values,
			String orderField, int orderType) throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, false, orderField,
				orderType, -1, -1);
	}

	public String selectWhereNot(String[] fields, String[] values,
			String orderField, int orderType) throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, true, orderField,
				orderType, -1, -1);
	}

	public String selectWhere(String[] fields, String[] values,
			String orderField, int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, false, orderField,
				orderType, currentPage, numPerPage);
	}

	public String selectWhereNot(String[] fields, String[] values,
			String orderField, int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, true, orderField,
				orderType, currentPage, numPerPage);
	}

	public String selectWhere(String[] fields, String orderField, int orderType)
			throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, false, orderField,
				orderType, -1, -1);
	}

	public String selectWhereNot(String[] fields, String orderField,
			int orderType) throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, true, orderField,
				orderType, -1, -1);
	}

	public String selectWhere(String[] fields, String orderField,
			int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, false, orderField,
				orderType, currentPage, numPerPage);
	}

	public String selectWhereNot(String[] fields, String orderField,
			int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, true, orderField,
				orderType, currentPage, numPerPage);
	}

	public String selectWhere(String[] fields, String[] values, int orderType)
			throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, false, null,
				orderType, -1, -1);
	}

	public String selectWhereNot(String[] fields, String[] values, int orderType)
			throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, true, null,
				orderType, -1, -1);
	}

	public String selectWhere(String[] fields, String[] values, int orderType,
			int currentPage, int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, false, null,
				orderType, currentPage, numPerPage);
	}

	public String selectWhereNot(String[] fields, String[] values,
			int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, true, null,
				orderType, currentPage, numPerPage);
	}

	public String selectWhere(String[] fields, int orderType)
			throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, false, null, orderType,
				-1, -1);
	}

	public String selectWhereNot(String[] fields, int orderType)
			throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, true, null, orderType,
				-1, -1);
	}

	public String selectWhere(String[] fields, int orderType, int currentPage,
			int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, false, null, orderType,
				currentPage, numPerPage);
	}

	public String selectWhereNot(String[] fields, int orderType,
			int currentPage, int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, true, null, orderType,
				currentPage, numPerPage);
	}

	public String selectWhere(String[] fields, String[] values)
			throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, false, null,
				OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhereNot(String[] fields, String[] values)
			throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, true, null,
				OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhere(String[] fields, String[] values,
			int currentPage, int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, false, null,
				OrderType.DESC_ORDER, currentPage, numPerPage);
	}

	public String selectWhereNot(String[] fields, String[] values,
			int currentPage, int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, values, 0, false, true, null,
				OrderType.DESC_ORDER, currentPage, numPerPage);
	}

	public String selectWhere(String... fields) throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, false, null,
				OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhereNot(String... fields) throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, true, null,
				OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhere(String[] fields, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, false, null,
				OrderType.DESC_ORDER, currentPage, numPerPage);
	}

	public String selectWhereNot(String[] fields, int currentPage,
			int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, null, 0, false, true, null,
				OrderType.DESC_ORDER, currentPage, numPerPage);
	}

	public String selectWhereLike(String[] fields, String[] values,
			int likeType, String orderField, int orderType)
			throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, false,
				orderField, orderType, -1, -1);
	}

	public String selectWhereNotLike(String[] fields, String[] values,
			int likeType, String orderField, int orderType)
			throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, true,
				orderField, orderType, -1, -1);
	}

	public String selectWhereLike(String[] fields, String[] values,
			int likeType, String orderField, int orderType, int currentPage,
			int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, false,
				orderField, orderType, currentPage, numPerPage);
	}

	public String selectWhereNotLike(String[] fields, String[] values,
			int likeType, String orderField, int orderType, int currentPage,
			int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, true,
				orderField, orderType, currentPage, numPerPage);
	}

	public String selectWhereLike(String[] fields, int likeType,
			String orderField, int orderType) throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, false,
				orderField, orderType, -1, -1);
	}

	public String selectWhereNotLike(String[] fields, int likeType,
			String orderField, int orderType) throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, true, orderField,
				orderType, -1, -1);
	}

	public String selectWhereLike(String[] fields, int likeType,
			String orderField, int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, false,
				orderField, orderType, currentPage, numPerPage);
	}

	public String selectWhereNotLike(String[] fields, int likeType,
			String orderField, int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, true, orderField,
				orderType, currentPage, numPerPage);
	}

	public String selectWhereLike(String[] fields, String[] values,
			int likeType, int orderType) throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, false, null,
				orderType, -1, -1);
	}

	public String selectWhereNotLike(String[] fields, String[] values,
			int likeType, int orderType) throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, true, null,
				orderType, -1, -1);
	}

	public String selectWhereLike(String[] fields, String[] values,
			int likeType, int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, false, null,
				orderType, currentPage, numPerPage);
	}

	public String selectWhereNotLike(String[] fields, String[] values,
			int likeType, int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, true, null,
				orderType, currentPage, numPerPage);
	}

	public String selectWhereLike(String[] fields, int likeType, int orderType)
			throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, false, null,
				orderType, -1, -1);
	}

	public String selectWhereNotLike(String[] fields, int likeType,
			int orderType) throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, true, null,
				orderType, -1, -1);
	}

	public String selectWhereLike(String[] fields, int likeType, int orderType,
			int currentPage, int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, false, null,
				orderType, currentPage, numPerPage);
	}

	public String selectWhereNotLike(String[] fields, int likeType,
			int orderType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, true, null,
				orderType, currentPage, numPerPage);
	}

	public String selectWhereLike(String[] fields, String[] values, int likeType)
			throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, false, null,
				OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhereNotLike(String[] fields, String[] values,
			int likeType) throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, true, null,
				OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhereLike(String[] fields, String[] values,
			int likeType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, false, null,
				OrderType.DESC_ORDER, currentPage, numPerPage);
	}

	public String selectWhereNotLike(String[] fields, String[] values,
			int likeType, int currentPage, int numPerPage)
			throws SqlCreateException {
		return this.selectWhere(fields, values, likeType, true, true, null,
				OrderType.DESC_ORDER, currentPage, numPerPage);
	}

	public String selectWhereLike(String[] fields, int likeType)
			throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, false, null,
				OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhereNotLike(String[] fields, int likeType)
			throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, true, null,
				OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhereLike(String[] fields, int likeType,
			int currentPage, int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, false, null,
				OrderType.DESC_ORDER, currentPage, numPerPage);
	}

	public String selectWhereNotLike(String[] fields, int likeType,
			int currentPage, int numPerPage) throws SqlCreateException {
		return this.selectWhere(fields, null, likeType, true, true, null,
				OrderType.DESC_ORDER, currentPage, numPerPage);
	}

	public String selectWhereLike(String... fields) throws SqlCreateException {
		return this.selectWhere(fields, null, LikeType.ALL_LIKE, true, false,
				null, OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhereNotLike(String... fields)
			throws SqlCreateException {
		return this.selectWhere(fields, null, LikeType.ALL_LIKE, true, true,
				null, OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhereLike(int currentPage, int numPerPage,
			String[] fields) throws SqlCreateException {
		return this.selectWhere(fields, null, LikeType.ALL_LIKE, true, false,
				null, OrderType.DESC_ORDER, -1, -1);
	}

	public String selectWhereNotLike(int currentPage, int numPerPage,
			String[] fields) throws SqlCreateException {
		return this.selectWhere(fields, null, LikeType.ALL_LIKE, true, true,
				null, OrderType.DESC_ORDER, -1, -1);
	}

	public String selectAll(String orderField, int oType) {
		if (orderField == null) {
			orderField = idColumn;
		} else {
			orderField = ORMConfigBeanUtil.getColumn(clazz, orderField);
		}

		String orderType = OrderType.ASC_ORDER == oType ? "ASC" : "DESC";

		return String.format("SELECT * FROM %s ORDER BY %s %s ;", table,
				orderField, orderType);
	}

	public String selectAll(int oType) {
		return selectAll(null, oType);
	}

	public String selectAll() {
		return selectAll(null, OrderType.DESC_ORDER);
	}

	public String selectOne() throws SqlCreateException {
		// 反射工具
		ReflectUtil ru = new ReflectUtil(this.t);
		StringBuilder condition = new StringBuilder();

		Method m = ru.getGetter(idField);
		if (m == null) {
			throw new SqlCreateException("can not find id getter");
		}
		try {
			condition.append(idColumn).append(" = '").append(m.invoke(this.t))
					.append("'");
		} catch (Exception e) {
			throw new SqlCreateException(m + " invoke exception "
					+ e.toString());
		}

		return String.format("SELECT * FROM %s WHERE %s ;", table,
				condition.toString());
	}

	public String nextOne(int orderType) throws SqlCreateException {
		return this.nextOrPre(1, true, orderType);
	}

	public String nextOne(String orderField, String value, int orderType)
			throws SqlCreateException {
		return this.nextOrPre(orderField, orderType, value, 1, true);
	}

	public String preOne(String orderField, String value, int orderType)
			throws SqlCreateException {
		return this.nextOrPre(orderField, orderType, value, 1, false);
	}

	public String preOne(int orderType) throws SqlCreateException {
		return this.nextOrPre(1, false, orderType);
	}

	public String nextOrPre(int num, boolean isNext, int orderType)
			throws SqlCreateException {
		Method m = new ReflectUtil(t).getGetter(idField);
		if (m == null)
			throw new SqlCreateException("can not fine id getter");
		String idVal = "";
		try {
			idVal = String.valueOf(m.invoke(t));
		} catch (Exception e) {
			throw new SqlCreateException(m + " invoke exception "
					+ e.toString());
		}
		return nextOrPre(idColumn, orderType, idVal, num, isNext);
	}

	public String nextOrPre(String orderField, int orderType, String offset,
			int num, boolean isNext) throws SqlCreateException {
		String oType = orderType == 1 ? "ASC" : "DESC";
		String type = isNext == true ? ">" : "<";

		if (DBType.MYSQL_DB.equalsIgnoreCase(dbType))
			return String.format(
					"SELECT * FROM %s WHERE %s %s %s ORDER BY %s %s LIMIT %s;",
					table, orderField, type, offset, orderField, oType,
					String.valueOf(num));
		else if (DBType.MSSQL2000_DB.equalsIgnoreCase(dbType)
				|| DBType.MSSQL2005_DB.equalsIgnoreCase(dbType))
			return String
					.format("SELECT TOP %s * FROM %s WHERE %s IN (SELECT %s FROM %s  WHERE %s %s %s ) ;",
							String.valueOf(num), table, orderField, orderField,
							table, orderField, type, offset);
		else
			throw new SqlCreateException(
					"do not support dataBase. only mysql | mssql");
	}

	/**
	 * 
	 * @param currPage
	 * @param numPerPage
	 * @param orderField
	 * @param oType
	 * @param condition
	 * @return
	 * @throws SqlCreateException
	 */
	public String divPage(int currPage, int numPerPage, String orderField,
			int oType, String condition) throws SqlCreateException {
		String sql = null;
		if (orderField == null) {
			orderField = idColumn;
		} else {
			orderField = ORMConfigBeanUtil.getColumn(clazz, orderField);
		}

		String orderType = OrderType.ASC_ORDER == oType ? "ASC" : "DESC";

		if (DBType.MYSQL_DB.equalsIgnoreCase(dbType)) {
			sql = "SELECT * FROM ${table} ${condition} ORDER BY ${orderField} ${orderType} LIMIT ${first}, ${numPerPage} ;";
			if (currPage > 0 && numPerPage > 0) {
				sql = sql.replace("${first}",
						String.valueOf((currPage - 1) * numPerPage));
				sql = sql.replace("${numPerPage}", String.valueOf(numPerPage));
			}
		} else if (DBType.MSSQL2000_DB.equalsIgnoreCase(dbType)
				|| DBType.MSSQL2005_DB.equalsIgnoreCase(dbType)) {

			sql = "SELECT TOP ${numPerPage} * FROM ${table} WHERE ${orderField} NOT IN (SELECT TOP ${first} ${orderField} FROM ${table} ORDER BY ${orderField} ${orderType}) ${condition} ORDER BY ${orderField} ${orderType} ;";
			if (currPage > 0 && numPerPage > 0) {
				sql = sql.replace("${numPerPage}", String.valueOf(numPerPage));
				sql = sql.replace("${first}",
						String.valueOf(numPerPage * (currPage - 1)));
			}
		} else {
			throw new SqlCreateException(
					"can not support dataBase. only mysql mssql");
		}

		if (currPage <= 0 || numPerPage <= 0)
			sql = "SELECT * FROM ${table} ${condition} ORDER BY ${orderField} ${orderType} ;";
		
		sql = sql.replace("${table}", table);
		sql = sql.replace("${orderField}", orderField);
		sql = sql.replace("${orderType}", orderType);
		if (condition != null) {
			if (DBType.MSSQL2000_DB.equalsIgnoreCase(dbType)
					|| DBType.MSSQL2005_DB.equalsIgnoreCase(dbType)) {
				sql = sql.replace("${condition}", " AND " + condition);
			} else {
				sql = sql.replace("${condition}", " WHERE " + condition);
			}
		} else {
			sql = sql.replace("${condition}", "");
		}

		return sql;
	}

	/**
	 * 分页查询 默认id排序
	 * 
	 * @param currPage
	 * @param numPerPage
	 * @param oType
	 * @return
	 * @throws SqlCreateException
	 */
	public String divPage(int currPage, int numPerPage, int oType)
			throws SqlCreateException {
		return this.divPage(currPage, numPerPage, null, oType, null);
	}

	public String divPage(int currPage, int numPerPage, String orderField,
			int oType) throws SqlCreateException {
		return this.divPage(currPage, numPerPage, orderField, oType, null);
	}

	public String divPage(int currPage, int numPerPage, int oType,
			String condition) throws SqlCreateException {
		return this.divPage(currPage, numPerPage, null, oType, condition);
	}

	/**
	 * 分页查询 默认id排序 且降序
	 * 
	 * @param currPage
	 * @param numPerPage
	 * @return
	 * @throws SqlCreateException
	 */
	public String divPage(int currPage, int numPerPage)
			throws SqlCreateException {
		return this.divPage(currPage, numPerPage, null, OrderType.DESC_ORDER,
				null);
	}

	public String divPage(int currPage, int numPerPage, String condition)
			throws SqlCreateException {
		return this.divPage(currPage, numPerPage, null, OrderType.DESC_ORDER,
				condition);
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
}
