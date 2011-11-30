package com.cfuture08.eweb4j.component.dwz.menu.action.treeMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.Result;
import com.cfuture08.util.StringUtil;

public class SearchAction extends BaseAction {

	@RequestMapping(value = "/search", method = "GET|POST")
	@Result(name = { "success" }, value = { "list.jsp" })
	public String doSearch() {
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
}
