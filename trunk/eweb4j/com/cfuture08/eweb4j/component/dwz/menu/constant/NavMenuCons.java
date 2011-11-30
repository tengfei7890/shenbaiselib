package com.cfuture08.eweb4j.component.dwz.menu.constant;

import com.cfuture08.eweb4j.component.dwz.view.CallBackJson;

public interface NavMenuCons {
	public final static String IOC_DAO_BEAN_ID = "NavMenuDAO";
	public final static String IOC_SERVICE_BEAN_ID = "NavMenuService";
	public final static String MODEL_NAME = "navMenus";
	public final static String MODULE_PATH = "admin/navMenus";
	public final static String DWZ_SUCCESS_JSON_RELOAD_NAVTAB = new CallBackJson(
			"200", "操作成功", "xssydhcd", "navMenus/list", "reloadTab", "显示所有导航菜单")
			.toString();
	public final static String DWZ_SUCCESS_JSON_CLOSE_CURRENT = new CallBackJson(
			"200", "操作成功", "xssydhcd", "navMenus/list", "closeCurrent",
			"显示所有导航菜单").toString();
}
