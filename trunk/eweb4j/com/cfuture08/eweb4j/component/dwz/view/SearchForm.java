package com.cfuture08.eweb4j.component.dwz.view;

/**
 * 查询表单
 * 
 * @author weiwei
 * 
 */
public class SearchForm {
	private String action;
	private String keyword;

	public SearchForm(String action, String keyword) {
		this.action = action;
		this.keyword = keyword;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
