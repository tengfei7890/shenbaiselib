package com.cfuture08.eweb4j.component.dwz.menu.domain.dao;

import java.util.List;

import com.cfuture08.eweb4j.component.dwz.menu.domain.model.NavMenu;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;

/**
 * 导航菜单数据库访问接口
 * @author weiwei
 *
 */
public interface NavMenuDAO {
	public NavMenu getOneByName(String name) throws MenuDataAccessException;

	public long countAll() throws MenuDataAccessException;

	public List<NavMenu> getPage(int p, int n)
			throws MenuDataAccessException;

	public NavMenu getOne(Long id) throws MenuDataAccessException;

	public void update(NavMenu navMenu) throws MenuDataAccessException;

	public void create(NavMenu navMenu) throws MenuDataAccessException;

	public List<NavMenu> getAll() throws MenuDataAccessException;

	public void deleteOne(Long id) throws MenuDataAccessException;

	public List<NavMenu> searchAndPaging(String keyword, int p, int n)
			throws MenuDataAccessException;
	
	public long countSearch(String keyword) throws MenuDataAccessException;
}
