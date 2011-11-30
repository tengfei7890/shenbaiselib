package com.cfuture08.eweb4j.component.dwz.menu.action.navMenu;

import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;

public class SuggestAction extends BaseAction {

	@RequestMapping(value = "/suggest.json", method = "GET|POST")
	public void doSuggest() {
		try {
			out.print(service.getAllFormatJson());
		} catch (Exception e) {
			out.print(dwz.getFailedJson(e.getMessage()));
		}
	}

}
