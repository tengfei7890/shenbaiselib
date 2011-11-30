package com.cfuture08.eweb4j.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cfuture08.eweb4j.mvc.action.IAction;
import com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean;

public abstract class Interceptor implements IAction {
	public HttpServletRequest request;
	public HttpServletResponse response;

	public abstract String intecept(String uri);

	public Interceptor init(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		this.request = request;
		this.response = response;
		return this;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String execute() {
		return null;
	}

	@Override
	public IAction init(HttpServletRequest request,
			HttpServletResponse response, ActionConfigBean contextBean)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	};
}
