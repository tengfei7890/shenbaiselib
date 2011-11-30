package com.cfuture08.eweb4j.component.dwz.menu.action.treeMenu;

import com.cfuture08.eweb4j.component.dwz.menu.constant.CommonCons;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.util.StringUtil;

public class LookupParentsAction extends BaseAction {

	@RequestMapping(value = "/{navMenuId}/lookup", method = "GET|POST")
	public String doLookup() {
		try {
			request.setAttribute("listPage", service.getParentsSearchResult(
					navMenuId, treeMenuId, keyword, pageNum, numPerPage));
		} catch (Exception e) {
			request.setAttribute(CommonCons.ERROR_ATTR_NAME,
					StringUtil.getExceptionString(e));
			return CommonCons.ERROR_PAGE;
		}

		return "../lookup.jsp";
	}

	@RequestMapping(value = "/{navMenuId}/lookupSearch", method = "GET|POST")
	public String doLookupSearch() {
		return this.doLookup();
	}
}
