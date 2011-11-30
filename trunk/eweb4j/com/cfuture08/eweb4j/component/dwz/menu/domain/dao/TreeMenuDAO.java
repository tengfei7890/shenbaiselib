package com.cfuture08.eweb4j.component.dwz.menu.domain.dao;

import java.util.List;

import com.cfuture08.eweb4j.component.dwz.menu.domain.model.TreeMenu;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;

public interface TreeMenuDAO {
	public long countAll() throws MenuDataAccessException;

	public List<TreeMenu> getPageWithCascade(int pageNum, int numPerPage)
			throws MenuDataAccessException;

	public long countSearch(String keyword) throws MenuDataAccessException;

	public List<TreeMenu> getSearchResult(String keyword, int pageNum,
			int numPerPage) throws MenuDataAccessException;

	public TreeMenu getOne(Long id) throws MenuDataAccessException;

	public void update(TreeMenu treeMenu) throws MenuDataAccessException;

	public List<TreeMenu> getParent(Long navMenuId, Long treeMenuId)
			throws MenuDataAccessException;

	public List<TreeMenu> getParentSearchResult(int pageNum, int numPerPage,
			String keyword, Long navMenuId, Long treeMenuId)
			throws MenuDataAccessException;

	public long countParentSearchResult(String keyword, Long navMenuId,
			Long treeMenuId) throws MenuDataAccessException;

	public void create(TreeMenu menu) throws MenuDataAccessException;

	public void deleteOne(Long id) throws MenuDataAccessException;

	public TreeMenu getOneByName(String name) throws MenuDataAccessException;

	public List<TreeMenu> getTopParentOrderByRankASC(Long navMenuId)
			throws MenuDataAccessException;

	public List<TreeMenu> getChildrenOrderByRankASC(Long pid)
			throws MenuDataAccessException;

}
