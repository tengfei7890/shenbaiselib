package com.cfuture08.eweb4j.orm.dao.base;

import java.util.List;

import com.cfuture08.eweb4j.orm.dao.DAOException;

/**
 * 
 * 基础DAO对象
 * 
 * @author weiwei
 * 
 */
public interface BaseDAO<T> {
	/**
	 * 获取所有
	 * 
	 * @return
	 */
	public List<T> getAll() throws DAOException;

	/**
	 * 查看详细
	 * 
	 * @param <T>
	 * @param id
	 * @return
	 */
	public T getOne(Number id) throws DAOException;

	/**
	 * 分页查看列表
	 * 
	 * @param <T>
	 * @param pageNum
	 * @param numPerPage
	 * @return
	 */
	public List<T> getPage(int pageNum, int numPerPage) throws DAOException;

	/**
	 * 添加
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
	public String create(T t) throws DAOException;

	/**
	 * 修改
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
	public String update(T t) throws DAOException;

	/**
	 * 修改
	 * 
	 * @param id
	 * @param fields
	 * @param values
	 * @return
	 */
	public String update(Number id, String[] fields, String[] values)
			throws DAOException;

	/**
	 * 删除
	 * 
	 * @param <T>
	 * @param ids
	 * @return
	 */
	public String delete(Number[] ids) throws DAOException;

	/**
	 * 总记录数
	 * 
	 * @return
	 */
	public long countAll() throws DAOException;

	/**
	 * 关键字检索，通过多个字段
	 * 
	 * @param field
	 * @param keyword
	 * @param pageNum
	 * @param numPerPage
	 * @param searchType
	 *            -2精确匹配 -1左匹配 0全匹配 1右匹配
	 * @return
	 */
	public List<T> searchByKeywordAndPaging(String[] field, String keyword,
			int pageNum, int numPerPage, int searchType) throws DAOException;

	/**
	 * 
	 * @param field
	 * @param keyword
	 * @param pageNum
	 * @param numPerPage
	 * @param searchType
	 * @param orderField
	 * @param orderType
	 * @return
	 */
	public List<T> searchByKeywordAndPagingAndSort(String[] field,
			String keyword, int pageNum, int numPerPage, int searchType,
			String orderField, int orderType) throws DAOException;

	/**
	 * 关键字检索，通过所有字段
	 * 
	 * @param keyword
	 * @param pageNum
	 * @param numPerPage
	 * @param searchType
	 *            -2精确匹配 -1左匹配 0全匹配 1右匹配
	 * @return
	 */
	public List<T> searchByKeywordAndPaging(String keyword, int pageNum,
			int numPerPage, int searchType) throws DAOException;

	/**
	 * 
	 * @param fields
	 * @param values
	 * @param pageNum
	 * @param numPerPage
	 * @param searchType
	 * @param isOR
	 * @param isNot
	 * @param orderField
	 * @param orderType
	 * @return
	 */
	public List<T> search(String[] fields, String[] values, int pageNum,
			int numPerPage, int searchType, boolean isOR, boolean isNot,
			String orderField, int orderType) throws DAOException;

	/**
	 * 
	 * @param fields
	 * @param t
	 * @param pageNum
	 * @param numPerPage
	 * @param searchType
	 * @param isOR
	 * @param orderField
	 * @param orderType
	 * @param isNot
	 * @return
	 */
	public List<T> search(String[] fields, T t, int pageNum, int numPerPage,
			int searchType, boolean isOR, boolean isNot, String orderField,
			int orderType) throws DAOException;

	public void cascadeSelect(String... fieldNames) throws DAOException;

	public Class<T> getClazz();
}
