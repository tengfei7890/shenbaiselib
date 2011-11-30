package com.cfuture08.eweb4j.orm.dao.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.orm.config.ORMConfigBeanUtil;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.dao.factory.DAOFactory;
import com.cfuture08.util.ReflectUtil;
import com.cfuture08.util.StringUtil;

public class BaseDAOImpl<T> implements BaseDAO<T> {
	protected final static String FAIL_ERROR = "操作失败，数据库错误类型，请联系管理员。";
	protected Class<T> clazz;
	protected T t;

	public BaseDAOImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	@Override
	public List<T> getAll() throws DAOException {
		return DAOFactory.getSelectDAO().selectAll(clazz);
	}

	@Override
	public T getOne(Number id) throws DAOException {
		String[] fields = new String[] { ORMConfigBeanUtil.getIdField(clazz) };
		String[] values = new String[] { String.valueOf(id) };
		return DAOFactory.getSelectDAO().selectOne(clazz, fields, values);
	}

	@Override
	public List<T> getPage(int pageNum, int numPerPage) throws DAOException {
		return DAOFactory.getDivPageDAO().divPage(clazz, pageNum, numPerPage);
	}

	@Override
	public String create(T t) throws DAOException {
		String error = null;
		int idVal = (Integer) DAOFactory.getInsertDAO().insert(t);
		if (idVal <= 0)
			error = FAIL_ERROR;
		String idField = ORMConfigBeanUtil.getIdField(t.getClass());
		ReflectUtil ru = new ReflectUtil(t);
		Method idSetter = ru.getSetter(idField);
		try {
			idSetter.invoke(t, idVal);
		} catch (Exception e) {
			error = "idSetter invoke exception.";
		} 
		
		return error;
	}

	@Override
	public String update(T t) throws DAOException {
		String error = null;
		int flag = (Integer) DAOFactory.getUpdateDAO().update(t);
		if (flag == 0)
			error = FAIL_ERROR;

		return error;
	}

	@Override
	public String update(Number id, String[] fields, String[] values)
			throws DAOException {
		String error = null;
		T t = getOne(id);
		if (t != null) {
			int flag = (Integer) DAOFactory.getUpdateDAO().updateByFieldIsValue(t,
					fields, values);
			if (flag == 0)
				error = FAIL_ERROR;

		} else
			error = "找不到记录";

		return error;
	}

	@Override
	public String delete(Number[] ids) throws DAOException {
		String error = null;
		StringBuilder sb = new StringBuilder();
		List<T> ts = new ArrayList<T>();
		for (int i = 0; i < ids.length; ++i) {
			Number id = ids[i];
			T t = getOne(id);
			if (t != null)
				ts.add(t);
			else
				sb.append("id[").append(i).append("]").append(":无效值，无法删除。\n");

		}
		if (ts.size() > 0) {
			Number[] flag = DAOFactory.getDeleteDAO().batchDelete(
					ts.toArray(new Object[] {}));
			if (flag == null)
				sb.append(FAIL_ERROR);

		}
		if (sb.length() > 0)
			error = sb.toString();

		return error;
	}

	@Override
	public long countAll() throws DAOException {
		return DAOFactory.getSelectDAO().selectCount(clazz);
	}

	@Override
	public List<T> searchByKeywordAndPaging(String[] fields, String keyword,
			int pageNum, int numPerPage, int searchType) throws DAOException {
		return this.searchByKeywordAndPagingAndSort(fields, keyword, pageNum,
				numPerPage, searchType, null, -1);
	}

	@Override
	public List<T> searchByKeywordAndPagingAndSort(String[] fields,
			String keyword, int pageNum, int numPerPage, int searchType,
			String orderField, int orderType) throws DAOException {
		List<T> list = null;
		String[] values = new String[fields.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = keyword;
		}
		if (-2 < searchType && searchType < 2) {
			list = DAOFactory.getSearchDAO().searchByDivPage(clazz, fields,
					values, searchType, true, false, true, orderField,
					orderType, pageNum, numPerPage);
		} else {
			list = DAOFactory.getSearchDAO().searchByDivPage(clazz, fields,
					values, searchType, false, false, true, null, -1, pageNum,
					numPerPage);
		}
		return list;
	}

	@Override
	public List<T> searchByKeywordAndPaging(String keyword, int pageNum,
			int numPerPage, int likeType) {
		List<T> list = null;
		try {
			String[] values = null;
			ReflectUtil ru = new ReflectUtil(clazz);
			Field[] fs = ru.getFields();
			List<String> fieldList = new ArrayList<String>();
			if (!StringUtil.isNumeric(keyword)) {
				// 如果关键字是非纯数字，只有非数字类型的字段才能被列入查询范围
				for (Field f : fs) {
					if (!Number.class.isAssignableFrom(f.getType())
							&& !int.class.isAssignableFrom(f.getType())
							&& !long.class.isAssignableFrom(f.getType())
							&& !float.class.isAssignableFrom(f.getType())
							&& !double.class.isAssignableFrom(f.getType())) {
						fieldList.add(f.getName());
					}
				}
			}
			String[] fields = null;
			if (fieldList.size() > 0) {
				fields = fieldList.toArray(new String[] {});
			} else {
				fields = ru.getFieldsName();
			}
			values = new String[fields.length];
			for (int i = 0; i < fields.length; ++i) {
				values[i] = keyword;
			}
			if (-2 < likeType && likeType < 2) {
				list = DAOFactory.getSearchDAO().searchByDivPage(clazz, fields,
						values, likeType, true, false, true, null, -1, pageNum,
						numPerPage);
			} else {
				list = DAOFactory.getSearchDAO().searchByDivPage(clazz, fields,
						values, likeType, false, false, true, null, -1,
						pageNum, numPerPage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	@Override
	public List<T> search(String[] fields, String[] values, int pageNum,
			int numPerPage, int searchType, boolean isOR, boolean isNot,
			String orderField, int orderType) throws DAOException {
		List<T> list = null;

		// 模糊搜索
		if (-2 < searchType && searchType < 2) {
			list = DAOFactory.getSearchDAO().searchByDivPage(clazz, fields,
					values, searchType, true, isNot, isOR, null, -1, pageNum,
					numPerPage);
		} else {
			// 精确搜索
			list = DAOFactory.getSearchDAO().searchByDivPage(clazz, fields,
					values, searchType, false, isNot, isOR, null, -1, pageNum,
					numPerPage);
		}

		return list;
	}

	@Override
	public List<T> search(String[] fields, T t, int pageNum, int numPerPage,
			int searchType, boolean isOR, boolean isNot, String orderField,
			int orderType) throws DAOException {
		List<T> list = null;
		// 模糊搜索
		if (-2 < searchType && searchType < 2) {
			list = DAOFactory.getSearchDAO().searchByDivPage(t, fields,
					searchType, true, isNot, isOR, null, -1, pageNum,
					numPerPage);
		} else {
			// 精确搜索
			list = DAOFactory.getSearchDAO().searchByDivPage(t, fields,
					searchType, false, isNot, isOR, null, -1, pageNum,
					numPerPage);
		}

		return list;
	}

	public void cascadeSelect(String... fieldName) {
		DAOFactory.getCascadeDAO().select(t, fieldName);
	}
}
