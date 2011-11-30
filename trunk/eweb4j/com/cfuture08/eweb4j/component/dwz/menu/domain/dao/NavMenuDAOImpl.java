package com.cfuture08.eweb4j.component.dwz.menu.domain.dao;

import java.util.List;

import com.cfuture08.eweb4j.component.dwz.menu.domain.model.NavMenu;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.dao.base.BaseDAO;
import com.cfuture08.eweb4j.orm.dao.base.BaseDAOImpl;
import com.cfuture08.eweb4j.orm.dao.factory.DAOFactory;

public class NavMenuDAOImpl implements NavMenuDAO {
	private BaseDAO<NavMenu> dao = new BaseDAOImpl<NavMenu>(NavMenu.class);

	@Override
	public NavMenu getOneByName(String name) throws MenuDataAccessException {
		NavMenu navMenu = null;
		try {
			navMenu = DAOFactory.getSelectDAO().selectOne(NavMenu.class,
					new String[] { "name" }, new String[] { name });
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return navMenu;
	}

	@Override
	public long countAll() throws MenuDataAccessException {
		long allCount = 0;
		try {
			allCount = dao.countAll();
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
		return allCount;
	}

	@Override
	public List<NavMenu> getPage(int p, int n) throws MenuDataAccessException {
		List<NavMenu> list = null;
		try {
			list = dao.getPage(p, n);
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
		return list;
	}

	@Override
	public NavMenu getOne(Long id) throws MenuDataAccessException {
		NavMenu navMenu = null;
		try {
			navMenu = dao.getOne(id);
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
		return navMenu;
	}

	@Override
	public void update(NavMenu navMenu) throws MenuDataAccessException {
		try {
			dao.update(navMenu);
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
	}

	@Override
	public void create(NavMenu navMenu) throws MenuDataAccessException {
		try {
			dao.create(navMenu);
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
	}

	@Override
	public List<NavMenu> getAll() throws MenuDataAccessException {
		List<NavMenu> list = null;
		try {
			list = dao.getAll();
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
		return list;
	}

	@Override
	public void deleteOne(Long id) throws MenuDataAccessException {
		try {
			dao.delete(new Long[] { id });
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
	}

	@Override
	public List<NavMenu> searchAndPaging(String keyword, int p, int n)
			throws MenuDataAccessException {
		List<NavMenu> list = null;
		try {
			String[] fields = new String[] { "name" };
			list = dao.searchByKeywordAndPaging(fields, keyword, p, n, 0);
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
		return list;
	}

	@Override
	public long countSearch(String keyword) throws MenuDataAccessException {
		long count = 0;
		
		try {
			String condition = "name like '%" + keyword + "%'";
			count = DAOFactory.getSelectDAO().selectCount(NavMenu.class,
					condition);
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
		return count;
	}

}
