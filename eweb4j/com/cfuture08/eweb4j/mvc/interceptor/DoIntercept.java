package com.cfuture08.eweb4j.mvc.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cfuture08.eweb4j.cache.InterConfigBeanCache;
import com.cfuture08.eweb4j.component.dwz.view.CallBackJson;
import com.cfuture08.eweb4j.mvc.config.bean.InterConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.Url;
import com.cfuture08.eweb4j.mvc.validate.ValidatorConstant;

public class DoIntercept {
	public static String intercept(String interType, String uri,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		String error = null;
		for (InterConfigBean inter : InterConfigBeanCache.getList()) {
			String _interType = inter.getType();
			if (interType.equals(_interType)) {
				for (Url u : inter.getUrl()) {
					String type = u.getType();
					String url = u.getValue();
					boolean flag = false;
					if ("*".equals(url)) {
						flag = true;
					} else if ("start".equalsIgnoreCase(type)) {
						// 以url开头
						if (uri.startsWith(url)) {
							flag = true;
						}
					} else if ("end".equalsIgnoreCase(type)) {
						// 以url结尾
						if (uri.endsWith(url)) {
							flag = true;
						}
					} else if ("contains".equalsIgnoreCase(type)) {
						// 包含url
						if (uri.contains(url)) {
							flag = true;
						}
					} else if ("all".equalsIgnoreCase(type)) {
						// 完全匹配url
						if (uri.equals(url)) {
							flag = true;
						}
					} else if ("regex".equalsIgnoreCase(type)) {
						// 正则匹配
						if (uri.matches(url)) {
							flag = true;
						}
					}

					if (flag) {
						for (String except : inter.getExcept()) {
							if (except.equals(uri)) {
								flag = false;
								break;
							}
						}
						if (flag) {
							Interceptor interceptor = (Interceptor) Class
									.forName(inter.getClazz()).newInstance();
							interceptor.init(req, res);
							error = interceptor.intecept(uri);
							if (error != null) {
								doIntercept(inter.getShowErrorType(), error,
										req, res);
								// 一旦找出错误，返回上层，否则继续向下执行其他拦截链
								return error;
							}
						}
					}
				}
			}
		}
		return error;
	}

	private static String doIntercept(String showErrorType, String error,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (error != null) {
			if (ValidatorConstant.DEFAULT_LOC.equals(showErrorType)
					|| ValidatorConstant.ALERT_LOC.equals(showErrorType)) {
				PrintWriter out = res.getWriter();
				out.print("<script>alert('"
						+ error
						+ "');javascript:history.go(-1)</script><center></center>");
				out.flush();
				out.close();
			} else if (ValidatorConstant.AJAX_OUT_LOC.equals(showErrorType)) {
				PrintWriter out = res.getWriter();
				out.print(error);
				out.flush();
				out.close();
			} else if (ValidatorConstant.DWZ_JSON_LOC.equals(showErrorType)) {
				PrintWriter out = res.getWriter();
				out.print(new CallBackJson(error));
				out.flush();
				out.close();
			} else if (ValidatorConstant.JAVASCRIPT_LOC.equals(showErrorType)) {
				PrintWriter out = res.getWriter();
				out.print("<script>" + error + "</script>");
				out.flush();
				out.close();
			} else {
				// 如果是填写跳转页面的话
				req.setAttribute("interError", error);
				req.getRequestDispatcher(showErrorType).forward(req, res);
			}
		}

		return null;
	}
}
