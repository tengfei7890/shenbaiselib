package com.cfuture08.eweb4j.component.dwz.menu.action.treeMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.TreeMenuCons;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.ValField;
import com.cfuture08.eweb4j.mvc.annotation.ValMess;
import com.cfuture08.eweb4j.mvc.annotation.Validator;

public class RemoveAction extends BaseAction {
	@RequestMapping(value = "/batchRemove", method = "DELETE", showValErr = "dwzJson")
	@Validator("required")
	@ValField("id")
	@ValMess("请选择要删除的信息")
	public void doBatchRemove() {
		try {
			service.batchRemove(ids);
			out.print(TreeMenuCons.DWZ_SUCCESS_JSON_RELOAD_NAVTAB);
		} catch (Exception e) {
			out.print(dwz.getFailedJson(e.getMessage()));
		}
	}

	@RequestMapping(value = "/{id}", method = "DELETE", showValErr = "dwzJson")
	@Validator("required")
	@ValField("id")
	@ValMess("请选择要删除的信息")
	public void doRemoveOne() {
		try {
			service.removeOne(id);
			out.print(TreeMenuCons.DWZ_SUCCESS_JSON_RELOAD_NAVTAB);
		} catch (Exception e) {
			out.print(dwz.getFailedJson(e.getMessage()));
		}
	}
}
