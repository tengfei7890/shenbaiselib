package com.cfuture08.eweb4j.component.dwz.menu.action.navMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.NavMenuCons;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.ValField;
import com.cfuture08.eweb4j.mvc.annotation.ValMess;
import com.cfuture08.eweb4j.mvc.annotation.Validator;

public class AddAction extends BaseAction {

	@RequestMapping(value = "/new", method = "GET|POST")
	public String doNew() {
		request.setAttribute("action", NavMenuCons.MODEL_NAME + "/");
		return "add.jsp";
	}

	@RequestMapping(value = "/", method = "POST", showValErr = "dwzJson")
	@Validator("int")
	@ValField({ "rank" })
	@ValMess(validator = { 0 }, field = { 0 }, value = { "排序项只能输入数字" })
	public void doAdd() {
		try {
			service.add(navMenu);
			out.print(NavMenuCons.DWZ_SUCCESS_JSON_RELOAD_NAVTAB);
		} catch (Exception e) {
			out.print(dwz.getFailedJson(e.getMessage()).toString());
		}
	}
}
