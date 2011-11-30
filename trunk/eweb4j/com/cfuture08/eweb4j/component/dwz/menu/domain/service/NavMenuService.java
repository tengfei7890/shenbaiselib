package com.cfuture08.eweb4j.component.dwz.menu.domain.service;

import com.cfuture08.eweb4j.component.dwz.menu.domain.model.NavMenu;
import com.cfuture08.eweb4j.component.dwz.menu.exception.DuplicateMenuNameException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuNotFoundException;
import com.cfuture08.eweb4j.component.dwz.view.EditPage;
import com.cfuture08.eweb4j.component.dwz.view.ListPage;

/**
 * 管理导航菜单服务, 向下依赖DAO, 向上Action提供服务接口, 数据库事务边界, 异常处理
 * 
 * @author weiwei
 * 
 */
public interface NavMenuService {

	/**
	 * 新增导航菜单信息
	 * 
	 * @param navMenu
	 * @throws DuplicateMenuNameException
	 * @throws MenuDataAccessException
	 *             数据库访问异常
	 */
	public void add(NavMenu navMenu) throws DuplicateMenuNameException,
			MenuDataAccessException;

	/**
	 * 获取给定数据库记录ID的导航菜单信息
	 * 
	 * @param id
	 * @return
	 * @throws MenuNotFoundException
	 * @throws MenuDataAccessException
	 *             数据库访问异常
	 */
	public NavMenu getOne(Long id) throws MenuNotFoundException,
			MenuDataAccessException;

	/**
	 * 获取编辑给定数据库记录ID的导航菜单页面信息
	 * 
	 * @param id
	 * @return
	 * @throws MenuNotFoundException
	 * @throws MenuDataAccessException
	 *             数据库访问异常
	 */
	public EditPage<NavMenu> getEditPage(Long id) throws MenuNotFoundException,
			MenuDataAccessException;

	/**
	 * 更新指定导航菜单信息
	 * 
	 * @param navMenu
	 * @throws MenuNotFoundException
	 *             无法找到给定的导航菜单信息
	 * @throws DuplicateMenuNameException
	 *             重复的导航菜单命名
	 * @throws MenuDataAccessException
	 *             数据库访问异常
	 */
	public void update(NavMenu navMenu) throws MenuNotFoundException,
			DuplicateMenuNameException, MenuDataAccessException;

	/**
	 * 获取所有导航菜单信息，Json格式字符串
	 * 
	 * @return String
	 * @throws MenuDataAccessException
	 */
	public String getAllFormatJson() throws MenuDataAccessException;

	/**
	 * 批量删除导航菜单信息，给定数据库记录ID
	 * 
	 * @param ids
	 * @throws MenuNotFoundException
	 * @throws MenuDataAccessException
	 */
	public void batchRemove(Long[] ids) throws MenuNotFoundException,
			MenuDataAccessException;

	/**
	 * 删除给定数据库记录ID的导航菜单信息
	 * 
	 * @param id
	 * @throws MenuNotFoundException
	 * @throws MenuDataAccessException
	 */
	public void removeOne(Long id) throws MenuNotFoundException,
			MenuDataAccessException;

	/**
	 * 分页
	 * 
	 * @param p
	 *            页码
	 * @param n
	 *            每页显示数
	 * @return
	 * @throws MenuDataAccessException
	 */
	public ListPage getPage(int p, int n) throws MenuDataAccessException;

	/**
	 * /** 分页查询
	 * 
	 * @param keyword
	 *            关键字
	 * @param p
	 *            页码
	 * @param n
	 *            每页显示数
	 * @return
	 * @throws MenuDataAccessException
	 */
	public ListPage getSearchResult(String keyword, int p, int n)
			throws MenuDataAccessException;

	/**
	 * 获取给定名字的导航菜单信息
	 * 
	 * @param name
	 * @return
	 * @throws MenuDataAccessException
	 * @throws MenuNotFoundException
	 */
	public NavMenu getOneByName(String name) throws MenuDataAccessException,
			MenuNotFoundException;
}
