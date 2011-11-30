package com.cfuture08.eweb4j.component.dwz.menu.action.treeMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.component.dwz.menu.constant.TreeMenuCons;
import com.cfuture08.eweb4j.mvc.annotation.Param;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.ValField;
import com.cfuture08.eweb4j.mvc.annotation.ValMess;
import com.cfuture08.eweb4j.mvc.annotation.Validator;
import com.cfuture08.util.StringUtil;

public class EditAction extends BaseAction {
	@RequestMapping(value = "/{id}/edit", method = "GET|POST", showValErr = "dwzJson")
	@Validator("required")
	@ValField("id")
	@ValMess("请选择要编辑的信息")
	public String doEdit() {
		try {
			request.setAttribute("editPage", service.getEditPage(id));
		} catch (Exception e) {
			request.setAttribute(CommonCons.ERROR_ATTR_NAME,
					StringUtil.getExceptionString(e));
			return CommonCons.ERROR_PAGE;
		}

		return "../edit.jsp";
	}

	@RequestMapping(value = "/{treeMenuId}", method = "PUT", showValErr = "dwzJson")
	@Validator({ "required", "int" })
	@ValField({ "navMenus.dwz_navMenu.navMenuId", "treeMenuId", "rank",
			TreeMenuCons.MODEL_NAME + ".dwz_treeMenu.treeMenuId" })
	@ValMess(validator = { 1, 0, 1, 1 }, field = { 0, 1, 2, 3 }, value = {
			"导航菜单ID应该为数字", "请选择要编辑的信息", "排序项只能输入数字", "父菜单ID应该为数字" })
	public void doUpdate(
			@Param("navMenus.dwz_navMenu.navMenuId") Long navMenuId,
			@Param(TreeMenuCons.MODEL_NAME + ".dwz_treeMenu.treeMenuId") Long pid) {
		try {
			service.updateWithCascade(treeMenu, navMenuId, pid);
			out.print(TreeMenuCons.DWZ_SUCCESS_JSON_CLOSE_CURRENT);
		} catch (Exception e) {
			out.print(dwz.getFailedJson(e.getMessage()));
		}
	}
}
