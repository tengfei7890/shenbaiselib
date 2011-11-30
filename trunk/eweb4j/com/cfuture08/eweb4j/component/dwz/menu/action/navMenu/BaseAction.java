package com.cfuture08.eweb4j.component.dwz.menu.action.navMenu;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import com.cfuture08.eweb4j.component.dwz.DWZ;
import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.component.dwz.menu.constant.NavMenuCons;
import com.cfuture08.eweb4j.component.dwz.menu.domain.model.NavMenu;
import com.cfuture08.eweb4j.component.dwz.menu.domain.service.NavMenuService;
import com.cfuture08.eweb4j.ioc.annotation.Ioc;
import com.cfuture08.eweb4j.mvc.annotation.Controller;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.Singleton;

/**
 * 抽象类 BaseAction
 * 
 * @author weiwei
 * 
 */
@Controller
@Singleton
@RequestMapping(NavMenuCons.MODULE_PATH)
public abstract class BaseAction {

	@Ioc(NavMenuCons.IOC_SERVICE_BEAN_ID)
	protected NavMenuService service = null;

	@Ioc(CommonCons.IOC_DWZ_BEAN_ID)
	protected DWZ dwz = null;

	protected HttpServletRequest request;
	protected NavMenu navMenu;
	protected PrintWriter out;

	protected Long id;
	protected Long[] ids;
	protected int pageNum = 1;
	protected int numPerPage = 20;
	protected String keyword;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public NavMenu getNavMenu() {
		return navMenu;
	}

	public void setNavMenu(NavMenu navMenu) {
		this.navMenu = navMenu;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setService(NavMenuService service) {
		this.service = service;
	}

	public void setDwz(DWZ dwz) {
		this.dwz = dwz;
	}

}
