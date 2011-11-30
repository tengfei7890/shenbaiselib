package com.cfuture08.eweb4j.component.dwz.menu.action.navMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.Result;
import com.cfuture08.util.StringUtil;

public class PagingAction extends BaseAction {

	@RequestMapping(value = "/list", method = "GET|POST")
	@Result("list.jsp")
	public String doPaging() {
		try {
			request.setAttribute("listPage",
					service.getPage(pageNum, numPerPage));
			request.setAttribute("random", Math.random());
		} catch (Exception e) {
			request.setAttribute(CommonCons.ERROR_ATTR_NAME,
					StringUtil.getExceptionString(e));
			return CommonCons.ERROR_PAGE;
		}

		return "success";
	}

	@RequestMapping(value = "/lookup", method = "GET|POST")
	@Result("lookup.jsp")
	public String doLookup() {
		return this.doPaging();
	}

}
