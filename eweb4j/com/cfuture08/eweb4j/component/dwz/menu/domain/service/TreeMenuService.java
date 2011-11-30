package com.cfuture08.eweb4j.component.dwz.menu.domain.service;

import java.util.List;

import com.cfuture08.eweb4j.component.dwz.menu.domain.model.TreeMenu;
import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotBeParentMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotFindNavMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotFindParentMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.DuplicateMenuNameException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuNotFoundException;
import com.cfuture08.eweb4j.component.dwz.view.EditPage;
import com.cfuture08.eweb4j.component.dwz.view.ListPage;

public interface TreeMenuService {

	/**
	 * 分页，级联NavMenu
	 * 
	 * @param pageNum
	 * @param numPerPage
	 * @return
	 * @throws MenuDataAccessException
	 */
	public ListPage getPageWithCascade(int pageNum, int numPerPage)
			throws MenuDataAccessException;

	/**
	 * 分页，查询关键字
	 * 
	 * @param keyword
	 * @param pageNum
	 * @param numPerPage
	 * @return
	 * @throws MenuDataAccessException
	 */
	public ListPage getSearchResult(String keyword, int pageNum, int numPerPage)
			throws MenuDataAccessException;

	/**
	 * 获取能够作为父菜单的列表。 条件：分页，查询关键字，所属某个导航菜单，不能把自己作为父菜单
	 * 
	 * @param navMenuId
	 *            所属导航菜单ID
	 * @param treeMenuId
	 *            自己(树形菜单)ID
	 * @param keyword
	 * @param pageNum
	 * @param numPerPage
	 * @return
	 * @throws MenuDataAccessException
	 * @throws CannotFindNavMenuException
	 */
	public ListPage getParentsSearchResult(Long navMenuId, Long treeMenuId,
			String keyword, int pageNum, int numPerPage)
			throws MenuDataAccessException, CannotFindNavMenuException;

	/**
	 * 获取能够作为父菜单的列表。 条件：分页，所属某个导航菜单，不能把自己作为父菜单
	 * 
	 * @param navMenuId
	 *            所属导航菜单ID
	 * @param treeMenuId
	 *            自己(树形菜单)ID
	 * @param pageNum
	 * @param numPerPage
	 * @return
	 * @throws MenuDataAccessException
	 * @throws CannotFindNavMenuException
	 */
	public ListPage getParentsPage(Long navMenuId, Long treeMenuId,
			int pageNum, int numPerPage) throws MenuDataAccessException,
			CannotFindNavMenuException;

	/**
	 * 获取能够作为父菜单的列表(Json格式字符串)。 条件：所属某个导航菜单，不能把自己作为父菜单
	 * 
	 * @param navMenuId
	 *            所属导航菜单ID
	 * @param treeMenuId
	 *            自己(树形菜单)ID
	 * @return
	 * @throws MenuDataAccessException
	 * @throws CannotFindNavMenuException
	 */
	public String getParentsFormatJson(Long navMenuId, Long treeMenuId)
			throws MenuDataAccessException, CannotFindNavMenuException;

	/**
	 * 批量删除给定的树形菜单
	 * 
	 * @param ids
	 * @throws MenuDataAccessException
	 * @throws MenuNotFoundException
	 */
	public void batchRemove(Long[] ids) throws MenuDataAccessException,
			MenuNotFoundException;

	/**
	 * 删除给定的树形菜单
	 * 
	 * @param id
	 * @throws MenuDataAccessException
	 * @throws MenuNotFoundException
	 */
	public void removeOne(Long id) throws MenuDataAccessException,
			MenuNotFoundException;

	/**
	 * 新增树形菜单. 规则： 自己不能作为父菜单、 导航菜单在数据库中要存在、 如果父菜单ID > 0，则父菜单在数据库中要存在、
	 * 
	 * @param treeMenu
	 * @param navMenuId
	 *            所属导航菜单ID
	 * @param pid
	 *            父菜单ID
	 * @throws MenuDataAccessException
	 * @throws CannotFindNavMenuException
	 * @throws CannotFindParentMenuException
	 * @throws CannotBeParentMenuException
	 * @throws DuplicateMenuNameException
	 *             ;
	 */
	public void addWithCascade(TreeMenu treeMenu, Long navMenuId, Long pid)
			throws MenuDataAccessException, CannotFindNavMenuException,
			CannotFindParentMenuException, CannotBeParentMenuException,
			DuplicateMenuNameException;

	/**
	 * 返回编辑页. 要求级联查询出与之有关的对象数据。
	 * 
	 * @param id
	 * @return
	 * @throws MenuDataAccessException
	 * @throws MenuNotFoundException
	 */
	public EditPage<TreeMenu> getEditPage(Long id)
			throws MenuDataAccessException, MenuNotFoundException;

	/**
	 * 更新树形菜单. 规则： 自己不能作为父菜单、 导航菜单在数据库中要存在、 如果父菜单ID > 0，则父菜单在数据库中要存在、
	 * 
	 * @param treeMenu
	 * @param navMenuId
	 *            所属导航菜单ID
	 * @param pid
	 *            父菜单ID
	 * @throws MenuDataAccessException
	 * @throws MenuNotFoundException
	 * @throws CannotFindNavMenuException
	 * @throws CannotFindParentMenuException
	 * @throws CannotBeParentMenuException
	 * @throws DuplicateMenuNameException
	 */
	public void updateWithCascade(TreeMenu treeMenu, Long navMenuId, Long pid)
			throws MenuDataAccessException, MenuNotFoundException,
			CannotFindNavMenuException, CannotFindParentMenuException,
			CannotBeParentMenuException, DuplicateMenuNameException;

	/**
	 * 获取某导航菜单下的所有顶级树形菜单
	 * 
	 * @param navMenuId
	 * @return
	 * @throws CannotFindNavMenuException
	 * @throws MenuDataAccessException
	 */
	public List<TreeMenu> getTopParent(Long navMenuId)
			throws CannotFindNavMenuException, MenuDataAccessException;

	/**
	 * 获取给定父菜单的所有子菜单
	 * @param pid
	 * @return
	 * @throws CannotFindParentMenuException
	 * @throws MenuDataAccessException
	 */
	public List<TreeMenu> getChildren(Long pid)
			throws CannotFindParentMenuException, MenuDataAccessException;
}
