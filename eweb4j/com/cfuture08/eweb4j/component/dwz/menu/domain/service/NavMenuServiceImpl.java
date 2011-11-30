package com.cfuture08.eweb4j.component.dwz.menu.domain.service;

import java.util.List;

import com.cfuture08.eweb4j.component.DivPageComp;
import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.component.dwz.menu.constant.NavMenuCons;
import com.cfuture08.eweb4j.component.dwz.menu.domain.dao.NavMenuDAO;
import com.cfuture08.eweb4j.component.dwz.menu.domain.model.NavMenu;
import com.cfuture08.eweb4j.component.dwz.menu.exception.DuplicateMenuNameException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuNotFoundException;
import com.cfuture08.eweb4j.component.dwz.view.EditPage;
import com.cfuture08.eweb4j.component.dwz.view.ListPage;
import com.cfuture08.eweb4j.component.dwz.view.SearchForm;

public class NavMenuServiceImpl implements NavMenuService {
	private SearchForm searchForm = new SearchForm(NavMenuCons.MODEL_NAME
			+ "/search", "");
	private List<NavMenu> pojos = null;
	private NavMenuDAO navMenuDAO = null;

	// IOC注入
	public void setNavMenuDAO(NavMenuDAO navMenuDAO) {
		if (this.navMenuDAO == null)
			this.navMenuDAO = navMenuDAO;
	}

	@Override
	public ListPage getPage(int p, int n) throws MenuDataAccessException {
		pojos = navMenuDAO.getPage(p, n);

		return new ListPage(NavMenuCons.MODEL_NAME, searchForm, pojos,
				new DivPageComp(p, n, navMenuDAO.countAll(),
						CommonCons.DIV_PAGE_BTN_MAX_SHOW));
	}

	@Override
	public EditPage<NavMenu> getEditPage(Long id) throws MenuNotFoundException,
			MenuDataAccessException {
		NavMenu navMenu = null;
		navMenu = navMenuDAO.getOne(id);
		if (navMenu == null)
			throw new MenuNotFoundException();

		return new EditPage<NavMenu>(NavMenuCons.MODEL_NAME,
				NavMenuCons.MODEL_NAME + "/" + navMenu.getNavMenuId(), navMenu);
	}

	@Override
	public NavMenu getOne(Long id) throws MenuNotFoundException,
			MenuDataAccessException {
		NavMenu navMenu = null;
		navMenu = navMenuDAO.getOne(id);
		if (navMenu == null)
			throw new MenuNotFoundException();

		return navMenu;
	}

	@Override
	public void update(NavMenu navMenu) throws MenuNotFoundException,
			DuplicateMenuNameException, MenuDataAccessException {
		NavMenu tmp_NavMenu = null;

		tmp_NavMenu = navMenuDAO.getOne(navMenu.getNavMenuId());
		if (tmp_NavMenu == null)
			throw new MenuNotFoundException();

		tmp_NavMenu = navMenuDAO.getOneByName(navMenu.getName());
		if (tmp_NavMenu != null
				&& !tmp_NavMenu.getNavMenuId().equals(navMenu.getNavMenuId()))
			throw new DuplicateMenuNameException();

		navMenuDAO.update(navMenu);

	}

	@Override
	public void add(NavMenu navMenu) throws DuplicateMenuNameException,
			MenuDataAccessException {
		NavMenu tmp_NavMenu = null;
		tmp_NavMenu = navMenuDAO.getOneByName(navMenu.getName());
		if (tmp_NavMenu != null)
			throw new DuplicateMenuNameException();

		navMenuDAO.create(navMenu);

	}

	@Override
	public String getAllFormatJson() throws MenuDataAccessException {
		List<NavMenu> list = navMenuDAO.getAll();
		return list == null ? "[]" : list.toString();
	}

	@Override
	public void batchRemove(Long[] ids) throws MenuNotFoundException,
			MenuDataAccessException {
		NavMenu navMenu = null;
		for (Long id : ids) {
			navMenu = navMenuDAO.getOne(id);
			if (navMenu == null)
				throw new MenuNotFoundException();

			navMenuDAO.deleteOne(id);
		}
	}

	@Override
	public void removeOne(Long id) throws MenuNotFoundException,
			MenuDataAccessException {
		NavMenu navMenu = navMenuDAO.getOne(id);
		if (navMenu == null)
			throw new MenuNotFoundException();

		navMenuDAO.deleteOne(id);
	}

	@Override
	public ListPage getSearchResult(String keyword, int p, int n)
			throws MenuDataAccessException {
		if (keyword == null || keyword.length() == 0) {
			searchForm.setKeyword("");
			return getPage(p, n);
		}

		List<NavMenu> navMenuList = navMenuDAO.searchAndPaging(keyword, p, n);
		searchForm.setKeyword(keyword);
		long count = navMenuDAO.countSearch(keyword);

		return new ListPage(NavMenuCons.MODEL_NAME, searchForm, navMenuList,
				new DivPageComp(p, n, count, CommonCons.DIV_PAGE_BTN_MAX_SHOW));
	}

	@Override
	public NavMenu getOneByName(String name) throws MenuDataAccessException,
			MenuNotFoundException {
		if (name == null || name.length() == 0)
			throw new MenuNotFoundException();

		NavMenu navMenu = this.navMenuDAO.getOneByName(name);
		if (navMenu == null)
			throw new MenuNotFoundException();

		return navMenu;
	}
}
