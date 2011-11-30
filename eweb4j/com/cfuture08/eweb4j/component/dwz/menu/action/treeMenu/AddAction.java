package com.cfuture08.eweb4j.component.dwz.menu.action.treeMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.TreeMenuCons;
import com.cfuture08.eweb4j.mvc.annotation.Param;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.ValField;
import com.cfuture08.eweb4j.mvc.annotation.ValMess;
import com.cfuture08.eweb4j.mvc.annotation.Validator;

public class AddAction extends BaseAction {
	@RequestMapping(value = "/new", method = "GET|POST")
	public String doNew() {
		request.setAttribute("model", TreeMenuCons.MODEL_NAME);
		return "add.jsp";
	}

	@RequestMapping(value = "/", method = "POST", showValErr = "dwzJson")
	@Validator({ "int", "required" })
	@ValField({ "rank", "navMenus.dwz_navMenu2.navMenuId" })
	@ValMess(validator = { 0, 1 }, field = { 0, 1 }, value = { "排序项只能输入数字",
			"导航菜单不能为空" })
	public void doAdd(@Param("navMenus.dwz_navMenu2.navMenuId") Long navMenuId,
			@Param(value = TreeMenuCons.MODEL_NAME
					+ ".dwz_treeMenu2.treeMenuId", init = "0") Long pid) {
		try {
			service.addWithCascade(treeMenu, navMenuId, pid);
			out.print(TreeMenuCons.DWZ_SUCCESS_JSON_CLOSE_CURRENT);
		} catch (Exception e) {
			out.print(dwz.getFailedJson(e.getMessage()));
		}
	}
}
