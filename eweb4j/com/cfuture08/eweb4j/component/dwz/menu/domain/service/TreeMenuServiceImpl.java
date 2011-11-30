package com.cfuture08.eweb4j.component.dwz.menu.domain.service;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.component.DivPageComp;
import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.component.dwz.menu.constant.TreeMenuCons;
import com.cfuture08.eweb4j.component.dwz.menu.domain.dao.NavMenuDAO;
import com.cfuture08.eweb4j.component.dwz.menu.domain.dao.TreeMenuDAO;
import com.cfuture08.eweb4j.component.dwz.menu.domain.model.NavMenu;
import com.cfuture08.eweb4j.component.dwz.menu.domain.model.TreeMenu;
import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotBeParentMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotFindNavMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotFindParentMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.DuplicateMenuNameException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuNotFoundException;
import com.cfuture08.eweb4j.component.dwz.view.EditPage;
import com.cfuture08.eweb4j.component.dwz.view.ListPage;
import com.cfuture08.eweb4j.component.dwz.view.SearchForm;

public class TreeMenuServiceImpl implements TreeMenuService {

	private SearchForm searchForm = new SearchForm(TreeMenuCons.MODEL_NAME
			+ "/search", "");

	private NavMenuDAO navMenuDAO = null;
	private TreeMenuDAO treeMenuDAO = null;

	// IOC注入
	public void setNavMenuDAO(NavMenuDAO navMenuDAO) {
		if (this.navMenuDAO == null)
			this.navMenuDAO = navMenuDAO;
	}

	// IOC注入
	public void setTreeMenuDAO(TreeMenuDAO treeMenuDAO) {
		if (this.treeMenuDAO == null)
			this.treeMenuDAO = treeMenuDAO;
	}

	@Override
	public ListPage getPageWithCascade(int pageNum, int numPerPage)
			throws MenuDataAccessException {
		List<TreeMenu> list = treeMenuDAO.getPageWithCascade(pageNum,
				numPerPage);
		DivPageComp dpc = new DivPageComp(pageNum, numPerPage,
				this.treeMenuDAO.countAll(), CommonCons.DIV_PAGE_BTN_MAX_SHOW);

		return new ListPage(TreeMenuCons.MODEL_NAME, searchForm, list, dpc);
	}

	@Override
	public ListPage getSearchResult(String keyword, int pageNum, int numPerPage)
			throws MenuDataAccessException {

		if (keyword == null || keyword.length() == 0) {
			searchForm.setKeyword("");
			return getPageWithCascade(pageNum, numPerPage);
		}

		List<TreeMenu> list = treeMenuDAO.getSearchResult(keyword, pageNum,
				numPerPage);

		DivPageComp dpc = new DivPageComp(pageNum, numPerPage,
				this.treeMenuDAO.countSearch(keyword),
				CommonCons.DIV_PAGE_BTN_MAX_SHOW);

		searchForm.setKeyword(keyword);

		return new ListPage(TreeMenuCons.MODEL_NAME, searchForm, list, dpc);
	}

	@Override
	public ListPage getParentsPage(Long navMenuId, Long treeMenuId,
			int pageNum, int numPerPage) throws MenuDataAccessException,
			CannotFindNavMenuException {
		return getParentsSearchResult(navMenuId, treeMenuId, "", pageNum,
				numPerPage);
	}

	@Override
	public ListPage getParentsSearchResult(Long navMenuId, Long treeMenuId,
			String keyword, int pageNum, int numPerPage)
			throws MenuDataAccessException, CannotFindNavMenuException {
		if (navMenuId == null || navMenuId <= 0)
			throw new CannotFindNavMenuException();

		NavMenu navMenu = this.navMenuDAO.getOne(navMenuId);
		if (navMenu == null)
			throw new CannotFindNavMenuException();

		if (keyword == null)
			keyword = "";

		List<TreeMenu> pojos = this.treeMenuDAO.getParentSearchResult(pageNum,
				numPerPage, keyword, navMenuId, treeMenuId);

		if (pojos == null)
			pojos = new ArrayList<TreeMenu>();
		TreeMenu tm = new TreeMenu();
		tm.setTreeMenuId(0L);
		tm.setName("主菜单-没有父节点");
		tm.setNavMenu(navMenu);
		pojos.add(tm);

		long allCount = this.treeMenuDAO.countParentSearchResult(keyword,
				navMenuId, treeMenuId);
		DivPageComp dpc = new DivPageComp(pageNum, numPerPage, allCount,
				CommonCons.DIV_PAGE_BTN_MAX_SHOW);

		searchForm.setAction(TreeMenuCons.MODEL_NAME + "/" + navMenuId
				+ "/lookupSearch");
		searchForm.setKeyword(keyword);

		return new ListPage(TreeMenuCons.MODEL_NAME, searchForm, pojos, dpc);
	}

	@Override
	public String getParentsFormatJson(Long navMenuId, Long treeMenuId)
			throws MenuDataAccessException, CannotFindNavMenuException {
		if (navMenuId == null || navMenuId <= 0)
			throw new CannotFindNavMenuException();

		NavMenu navMenu = this.navMenuDAO.getOne(navMenuId);
		if (navMenu == null)
			throw new CannotFindNavMenuException();

		List<TreeMenu> pojos = this.treeMenuDAO
				.getParent(navMenuId, treeMenuId);
		if (pojos == null)
			pojos = new ArrayList<TreeMenu>();

		TreeMenu tm = new TreeMenu();
		tm.setTreeMenuId(0L);
		tm.setName("主菜单-没有父节点");
		tm.setNavMenu(navMenu);

		pojos.add(tm);

		return pojos.toString();
	}

	@Override
	public void batchRemove(Long[] ids) throws MenuDataAccessException,
			MenuNotFoundException {
		TreeMenu treeMenu = null;
		for (Long id : ids) {
			treeMenu = treeMenuDAO.getOne(id);
			if (treeMenu == null)
				throw new MenuNotFoundException();

			treeMenuDAO.deleteOne(id);
		}
	}

	@Override
	public void removeOne(Long id) throws MenuDataAccessException,
			MenuNotFoundException {
		TreeMenu treeMenu = treeMenuDAO.getOne(id);
		if (treeMenu == null)
			throw new MenuNotFoundException();

		treeMenuDAO.deleteOne(id);
	}

	@Override
	public void addWithCascade(TreeMenu treeMenu, Long navMenuId, Long pid)
			throws MenuDataAccessException, CannotFindNavMenuException,
			CannotFindParentMenuException, CannotBeParentMenuException,
			DuplicateMenuNameException {

		TreeMenu tmp_TreeMenu = this.treeMenuDAO.getOneByName(treeMenu
				.getName());
		if (tmp_TreeMenu != null)
			throw new DuplicateMenuNameException();

		if (navMenuId == null || navMenuId <= 0)
			throw new CannotFindNavMenuException();

		NavMenu navMenu = this.navMenuDAO.getOne(navMenuId);
		if (navMenu == null)
			throw new CannotFindNavMenuException();

		if (pid > 0) {
			TreeMenu parent = this.treeMenuDAO.getOne(pid);
			if (parent == null)
				throw new CannotFindParentMenuException();
			if (pid.equals(treeMenu.getTreeMenuId()))
				throw new CannotBeParentMenuException();
			treeMenu.setParent(parent);
		} else {
			treeMenu.setParent(new TreeMenu());
		}

		treeMenu.setNavMenu(navMenu);

		String reloadFlag = treeMenu.getReloadFlag();
		treeMenu.setReloadFlag("on".equals(reloadFlag) ? "1" : "0");

		this.treeMenuDAO.create(treeMenu);
	}

	@Override
	public EditPage<TreeMenu> getEditPage(Long id)
			throws MenuDataAccessException, MenuNotFoundException {

		TreeMenu treeMenu = this.treeMenuDAO.getOne(id);
		if (treeMenu == null)
			throw new MenuNotFoundException();

		String reloadFlag = treeMenu.getReloadFlag();
		treeMenu.setReloadFlag("1".equals(reloadFlag) ? "checked" : "");

		EditPage<TreeMenu> editPage = new EditPage<TreeMenu>(
				TreeMenuCons.MODEL_NAME, TreeMenuCons.MODEL_NAME + "/" + id,
				treeMenu);

		return editPage;
	}

	@Override
	public void updateWithCascade(TreeMenu treeMenu, Long navMenuId, Long pid)
			throws MenuDataAccessException, MenuNotFoundException,
			CannotFindNavMenuException, CannotFindParentMenuException,
			CannotBeParentMenuException, DuplicateMenuNameException {
		Long id = treeMenu.getTreeMenuId();
		if (id == null || id <= 0)
			throw new MenuNotFoundException();

		TreeMenu tmp_TreeMenu = this.treeMenuDAO.getOne(treeMenu
				.getTreeMenuId());
		if (tmp_TreeMenu == null)
			throw new MenuNotFoundException();

		tmp_TreeMenu = this.treeMenuDAO.getOneByName(treeMenu.getName());
		if (tmp_TreeMenu != null
				&& !tmp_TreeMenu.getTreeMenuId().equals(
						treeMenu.getTreeMenuId()))
			throw new DuplicateMenuNameException();

		if (navMenuId == null || navMenuId <= 0)
			throw new CannotFindNavMenuException();
		NavMenu navMenu = this.navMenuDAO.getOne(navMenuId);
		if (navMenu == null)
			throw new CannotFindNavMenuException();

		if (pid > 0) {
			TreeMenu parent = this.treeMenuDAO.getOne(pid);
			if (parent == null)
				throw new CannotFindParentMenuException();
			if (pid.equals(treeMenu.getTreeMenuId()))
				throw new CannotBeParentMenuException();
			treeMenu.setParent(parent);
		}else {
			treeMenu.setParent(new TreeMenu());
		}

		treeMenu.setNavMenu(navMenu);

		String reloadFlag = treeMenu.getReloadFlag();
		treeMenu.setReloadFlag("on".equals(reloadFlag) ? "1" : "0");

		this.treeMenuDAO.update(treeMenu);
	}

	@Override
	public List<TreeMenu> getTopParent(Long navMenuId)
			throws CannotFindNavMenuException, MenuDataAccessException {
		if (navMenuId == null || navMenuId <= 0)
			throw new CannotFindNavMenuException();

		NavMenu navMenu = this.navMenuDAO.getOne(navMenuId);
		if (navMenu == null)
			throw new CannotFindNavMenuException();

		return this.treeMenuDAO.getTopParentOrderByRankASC(navMenuId);
	}

	@Override
	public List<TreeMenu> getChildren(Long pid)
			throws CannotFindParentMenuException, MenuDataAccessException {
		if (pid == null || pid <= 0)
			throw new CannotFindParentMenuException();

		TreeMenu parent = this.treeMenuDAO.getOne(pid);
		if (parent == null)
			throw new CannotFindParentMenuException();

		return this.treeMenuDAO.getChildrenOrderByRankASC(pid);
	}
}
