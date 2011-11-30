package com.cfuture08.eweb4j.component.dwz.menu.domain.dao;

import java.util.List;

import com.cfuture08.eweb4j.component.dwz.menu.domain.model.TreeMenu;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.dao.base.BaseDAO;
import com.cfuture08.eweb4j.orm.dao.base.BaseDAOImpl;
import com.cfuture08.eweb4j.orm.dao.factory.DAOFactory;
import com.cfuture08.eweb4j.orm.sql.constant.OrderType;

public class TreeMenuDAOImpl implements TreeMenuDAO {
	private BaseDAO<TreeMenu> dao = new BaseDAOImpl<TreeMenu>(TreeMenu.class);

	@Override
	public List<TreeMenu> getPageWithCascade(int pageNum, int numPerPage)
			throws MenuDataAccessException {
		List<TreeMenu> list = null;
		try {
			list = dao.getPage(pageNum, numPerPage);
			if (list != null)
				DAOFactory.getCascadeDAO().select(
						list.toArray(new TreeMenu[] {}), "parent", "navMenu");
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return list;
	}

	@Override
	public long countAll() throws MenuDataAccessException {
		long count = 0;
		try {
			count = dao.countAll();
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return count;
	}

	@Override
	public long countSearch(String keyword) throws MenuDataAccessException {
		long count = 0;
		try {
			String condition = "name like '%" + keyword + "%'";
			count = DAOFactory.getSelectDAO().selectCount(TreeMenu.class,
					condition);
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return count;
	}

	@Override
	public List<TreeMenu> getSearchResult(String keyword, int pageNum,
			int numPerPage) throws MenuDataAccessException {
		List<TreeMenu> list = null;
		try {
			String[] fields = new String[] { "name" };
			list = dao.searchByKeywordAndPaging(fields, keyword, pageNum,
					numPerPage, 0);
			if (list != null)
				DAOFactory.getCascadeDAO().select(
						list.toArray(new TreeMenu[] {}), "parent", "navMenu");
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return list;
	}

	@Override
	public TreeMenu getOne(Long id) throws MenuDataAccessException {
		TreeMenu treeMenu = null;
		try {
			treeMenu = dao.getOne(id);
			if (treeMenu != null)
				DAOFactory.getCascadeDAO()
						.select(treeMenu, "navMenu", "parent");
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return treeMenu;
	}

	@Override
	public void update(TreeMenu treeMenu) throws MenuDataAccessException {
		try {
			DAOFactory.getUpdateDAO().update(treeMenu);
			DAOFactory.getCascadeDAO().update(treeMenu, "parent", "navMenu");
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
	}

	@Override
	public void create(TreeMenu treeMenu) throws MenuDataAccessException {
		try {
			Long treeMenuId = Long.parseLong(""
					+ DAOFactory.getInsertDAO().insert(treeMenu));
			treeMenu.setTreeMenuId(treeMenuId);
			DAOFactory.getCascadeDAO().update(treeMenu, "parent", "navMenu");
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
	}

	@Override
	public List<TreeMenu> getParent(Long navMenuId, Long treeMenuId)
			throws MenuDataAccessException {
		List<TreeMenu> pojos = null;
		try {
			String condition = "id <> ? and navMenuId = ? ";
			pojos = DAOFactory.getSelectDAO().selectWhere(TreeMenu.class,
					condition, treeMenuId, navMenuId);
			if (pojos != null) 
				DAOFactory.getCascadeDAO().select(
						pojos.toArray(new TreeMenu[] {}), "parent", "navMenu");
			
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return pojos;
	}

	@Override
	public List<TreeMenu> getParentSearchResult(int pageNum, int numPerPage,
			String keyword, Long navMenuId, Long treeMenuId)
			throws MenuDataAccessException {
		List<TreeMenu> pojos = null;
		try {
			String condition = "id <> ? and navMenuId = ? and name like ? ";
			Object[] args = new Object[] { treeMenuId, navMenuId,
					"%" + keyword + "%" };
			pojos = DAOFactory.getDivPageDAO().divPageByWhere(TreeMenu.class,
					pageNum, numPerPage, condition, args);
			if (pojos != null) 
				DAOFactory.getCascadeDAO().select(
						pojos.toArray(new TreeMenu[] {}), "parent", "navMenu");
			
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return pojos;
	}

	@Override
	public long countParentSearchResult(String keyword, Long navMenuId,
			Long treeMenuId) throws MenuDataAccessException {
		long count = 0;
		try {
			String condition = "id <> '" + treeMenuId + "' and navMenuId = '"
					+ navMenuId + "' and name like '%" + keyword + "%'";
			count = DAOFactory.getSelectDAO().selectCount(TreeMenu.class,
					condition);
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}
		return count;
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
	public TreeMenu getOneByName(String name) throws MenuDataAccessException {
		TreeMenu treeMenu = null;
		try {
			treeMenu = DAOFactory.getSelectDAO().selectOne(TreeMenu.class,
					new String[] { "name" }, new String[] { name });
		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return treeMenu;
	}

	@Override
	public List<TreeMenu> getTopParentOrderByRankASC(Long navMenuId)
			throws MenuDataAccessException {
		List<TreeMenu> pojos = null;
		try {
			String[] fields = new String[] { "pid", "navMenuId" };
			String[] values = new String[] { "0", String.valueOf(navMenuId) };

			pojos = DAOFactory.getSearchDAO().searchByExact(TreeMenu.class,
					fields, values, "rank", OrderType.ASC_ORDER, false);

		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return pojos;
	}

	@Override
	public List<TreeMenu> getChildrenOrderByRankASC(Long pid)
			throws MenuDataAccessException {
		List<TreeMenu> pojos = null;
		try {
			String[] fields = new String[] { "pid" };
			String[] values = new String[] { String.valueOf(pid) };

			pojos = DAOFactory.getSearchDAO().searchByExact(TreeMenu.class,
					fields, values, "rank", OrderType.ASC_ORDER, false);

		} catch (DAOException e) {
			throw new MenuDataAccessException(e.getMessage());
		}

		return pojos;
	}
}
