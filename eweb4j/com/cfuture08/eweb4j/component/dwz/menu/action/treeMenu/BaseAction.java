package com.cfuture08.eweb4j.component.dwz.menu.action.treeMenu;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import com.cfuture08.eweb4j.component.dwz.DWZ;
import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.component.dwz.menu.constant.TreeMenuCons;
import com.cfuture08.eweb4j.component.dwz.menu.domain.model.TreeMenu;
import com.cfuture08.eweb4j.component.dwz.menu.domain.service.TreeMenuService;
import com.cfuture08.eweb4j.ioc.annotation.Ioc;
import com.cfuture08.eweb4j.mvc.annotation.Controller;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.Singleton;

/**
 * 抽象类BaseAction
 * 
 * @author weiwei
 * 
 */
@Controller
@Singleton
@RequestMapping(TreeMenuCons.MODULE_PATH)
public abstract class BaseAction {
	@Ioc(TreeMenuCons.IOC_SERVICE_BEAN_ID)
	protected TreeMenuService service = null;
	@Ioc(CommonCons.IOC_DWZ_BEAN_ID)
	protected DWZ dwz = null;

	protected HttpServletRequest request;
	protected PrintWriter out;

	protected TreeMenu treeMenu;

	protected Long[] ids;
	protected Long id = 0L;
	protected String keyword = "";
	protected Long treeMenuId = 0L;
	protected Long navMenuId = 0L;
	protected int pageNum = 1;
	protected int numPerPage = 20;

	public TreeMenu getTreeMenu() {
		return treeMenu;
	}

	public void setTreeMenu(TreeMenu treeMenu) {
		this.treeMenu = treeMenu;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public Long getTreeMenuId() {
		return treeMenuId;
	}

	public void setTreeMenuId(Long treeMenuId) {
		this.treeMenuId = treeMenuId;
	}

	public Long getNavMenuId() {
		return navMenuId;
	}

	public void setNavMenuId(Long navMenuId) {
		this.navMenuId = navMenuId;
	}

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setService(TreeMenuService service) {
		this.service = service;
	}

	public void setDwz(DWZ dwz) {
		this.dwz = dwz;
	}
	

}
