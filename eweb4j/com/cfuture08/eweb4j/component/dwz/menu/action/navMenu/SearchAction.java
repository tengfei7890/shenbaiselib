package com.cfuture08.eweb4j.component.dwz.menu.action.navMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.Result;
import com.cfuture08.util.StringUtil;

public class SearchAction extends BaseAction {

	@RequestMapping(value = "/search", method = "GET|POST")
	@Result("list.jsp")
	public String doSearchAndPaging() {
		try {
			request.setAttribute("listPage",
					service.getSearchResult(keyword, pageNum, numPerPage));
		} catch (Exception e) {
			request.setAttribute(CommonCons.ERROR_ATTR_NAME,
					StringUtil.getExceptionString(e));
			return CommonCons.ERROR_PAGE;
		}

		return "success";
	}

	@RequestMapping(value = "/lookupSearch", method = "GET|POST")
	@Result("lookup.jsp")
	public String doLookupSearch() {
		return this.doSearchAndPaging();
	}

}
