package com.cfuture08.eweb4j.component.dwz.menu.action.navMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.component.dwz.menu.constant.NavMenuCons;
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

	@RequestMapping(value = "/{navMenuId}", method = "PUT", showValErr = "dwzJson")
	@Validator({ "required", "int" })
	@ValField({ "navMenuId", "rank" })
	@ValMess(validator = { 0, 1 }, field = { 0, 1 }, value = { "请选择要编辑的信息",
			"排序项只能输入数字" })
	public void doUpdate() {
		try {
			service.update(navMenu);
			out.print(NavMenuCons.DWZ_SUCCESS_JSON_RELOAD_NAVTAB);
		} catch (Exception e) {
			out.print(dwz.getFailedJson(e.getMessage()));
		}
	}
}
