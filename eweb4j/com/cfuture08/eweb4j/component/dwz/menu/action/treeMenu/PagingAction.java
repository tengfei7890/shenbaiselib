package com.cfuture08.eweb4j.component.dwz.menu.action.treeMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.util.StringUtil;

public class PagingAction extends BaseAction {

	@RequestMapping(value = "/list", method = "GET|POST")
	public String doPaging() {
		try {
			request.setAttribute("listPage",
					service.getPageWithCascade(pageNum, numPerPage));
			request.setAttribute("random", Math.random());
		} catch (Exception e) {
			request.setAttribute(CommonCons.ERROR_ATTR_NAME,
					StringUtil.getExceptionString(e));
			return CommonCons.ERROR_PAGE;
		}

		return "list.jsp";
	}
}
