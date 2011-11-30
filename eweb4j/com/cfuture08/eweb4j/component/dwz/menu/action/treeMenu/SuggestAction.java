package com.cfuture08.eweb4j.component.dwz.menu.action.treeMenu;

import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.ValField;
import com.cfuture08.eweb4j.mvc.annotation.ValMess;
import com.cfuture08.eweb4j.mvc.annotation.Validator;

public class SuggestAction extends BaseAction {
	@RequestMapping(value = "/{navMenuId}/suggest.json", method = "GET|POST")
	@Validator("int")
	@ValField("navMenuId")
	@ValMess("导航菜单ID应该为数字格式")
	public void doSuggest() {
		try {
			out.print(service.getParentsFormatJson(navMenuId, treeMenuId));
		} catch (Exception e) {
			out.print(dwz.getFailedJson(e.getMessage()));
		}
	}
}
