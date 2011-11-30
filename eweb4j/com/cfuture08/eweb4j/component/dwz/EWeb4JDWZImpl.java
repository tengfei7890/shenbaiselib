package com.cfuture08.eweb4j.component.dwz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cfuture08.eweb4j.component.dwz.menu.domain.model.NavMenu;
import com.cfuture08.eweb4j.component.dwz.menu.domain.model.TreeMenu;
import com.cfuture08.eweb4j.component.dwz.menu.domain.service.NavMenuService;
import com.cfuture08.eweb4j.component.dwz.menu.domain.service.TreeMenuService;
import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotFindNavMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotFindParentMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuNotFoundException;
import com.cfuture08.eweb4j.component.dwz.view.ATag;
import com.cfuture08.eweb4j.component.dwz.view.Accordion;
import com.cfuture08.eweb4j.component.dwz.view.CallBackJson;
import com.cfuture08.eweb4j.component.dwz.view.LiTag;
import com.cfuture08.eweb4j.component.dwz.view.UlTag;

public class EWeb4JDWZImpl implements DWZ {

	private TreeMenuService treeMenuService = null;
	private NavMenuService navMenuService = null;

	// IOC注入
	public void setTreeMenuService(TreeMenuService treeMenuService) {
		if (this.treeMenuService == null)
			this.treeMenuService = treeMenuService;
	}

	// IOC注入
	public void setNavMenuService(NavMenuService navMenuService) {
		if (this.navMenuService == null)
			this.navMenuService = navMenuService;
	}

	@Override
	public CallBackJson getSuccessJson(String message, String navTabId,
			String forwardUrl, String callType, String title) {
		return new CallBackJson("200", message, navTabId, forwardUrl, callType,
				title);
	}

	@Override
	public CallBackJson getFailedJson(String message) {
		return new CallBackJson("300", message, "", "", "reload", "");
	}

	@Override
	public String getAccordion(String navMenuName)
			throws CannotFindNavMenuException, MenuDataAccessException,
			MenuNotFoundException, CannotFindParentMenuException {
		NavMenu navMenu = this.navMenuService.getOneByName(navMenuName);
		return getAccordion(navMenu.getNavMenuId());
	}

	@Override
	public String getAccordion(Long navMenuId)
			throws CannotFindNavMenuException, MenuDataAccessException,
			CannotFindParentMenuException {
		List<TreeMenu> menuList = this.treeMenuService.getTopParent(navMenuId);
		if (menuList == null)
			return "";

		StringBuilder sb = new StringBuilder();
		for (Iterator<TreeMenu> it = menuList.iterator(); it.hasNext();) {
			TreeMenu m = it.next();
			sb.append(new Accordion(m.getName(), getUlTag(m.getTreeMenuId()))
					.toString());
		}

		return sb.toString();
	}

	@Override
	public String createTreeMenu(Long rootNodeId)
			throws CannotFindParentMenuException, MenuDataAccessException {
		return getUlTag(rootNodeId).toString();
	}

	@Override
	public UlTag getUlTag(Long rootNodeId)
			throws CannotFindParentMenuException, MenuDataAccessException {
		return new UlTag("tree treeFolder", createTree(rootNodeId));
	}

	private List<LiTag> createTree(Long pid)
			throws CannotFindParentMenuException, MenuDataAccessException {
		List<LiTag> liList = new ArrayList<LiTag>();
		List<TreeMenu> menuList = this.treeMenuService.getChildren(pid);
		if (menuList == null)
			return liList;

		for (Iterator<TreeMenu> it = menuList.iterator(); it.hasNext();) {
			TreeMenu m = it.next();
			liList.add(new LiTag(new ATag(m.getTarget(), m.getRel(), m
					.getReloadFlag(), m.getHref(), m.getName()), new UlTag("",
					createTree(m.getTreeMenuId()))));
		}

		return liList;
	}

}
