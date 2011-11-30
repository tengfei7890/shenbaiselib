package com.cfuture08.eweb4j.component.dwz.menu.constant;

import com.cfuture08.eweb4j.component.dwz.view.CallBackJson;

public interface TreeMenuCons {
	public final static String IOC_DAO_BEAN_ID = "TreeMenuDAO";
	public final static String IOC_SERVICE_BEAN_ID = "TreeMenuService";
	public final static String MODEL_NAME = "treeMenus";
	public final static String MODULE_PATH = "admin/treeMenus";
	public final static String DWZ_SUCCESS_JSON_RELOAD_NAVTAB = new CallBackJson(
			"200", "操作成功", "xssysxcd", "treeMenus/list", "reloadTab",
			"显示所有树形菜单").toString();
	public final static String DWZ_SUCCESS_JSON_CLOSE_CURRENT = new CallBackJson(
			"200", "操作成功", "xssysxcd", "treeMenus/list", "closeCurrent",
			"显示所有树形菜单").toString();
}
