package com.cfuture08.eweb4j.component.dwz.menu.action.navMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.NavMenuCons;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.ValField;
import com.cfuture08.eweb4j.mvc.annotation.ValMess;
import com.cfuture08.eweb4j.mvc.annotation.Validator;

public class RemoveAction extends BaseAction {

	@RequestMapping(value = "/batchRemove", method = "DELETE", showValErr = "dwzJson")
	@Validator("required")
	@ValField("ids")
	@ValMess("请选择要删除的信息")
	public void doRemoveMulti() {
		try {
			service.batchRemove(ids);
			out.print(NavMenuCons.DWZ_SUCCESS_JSON_RELOAD_NAVTAB);
		} catch (Exception e) {
			out.print(dwz.getFailedJson(e.getMessage()).toString());
		}
	}

	@RequestMapping(value = "/{id}", method = "DELETE", showValErr = "dwzJson")
	@Validator("required")
	@ValField("id")
	@ValMess("请选择要删除的信息")
	public void doRemoveOne() {
		try {
			this.service.removeOne(id);
			this.out.print(NavMenuCons.DWZ_SUCCESS_JSON_RELOAD_NAVTAB);
		} catch (Exception e) {
			this.out.print(dwz.getFailedJson(e.getMessage()).toString());
		}
	}
}
