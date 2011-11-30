package com.cfuture08.eweb4j.mvc.validate;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cfuture08.eweb4j.component.dwz.view.CallBackJson;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;

public class ValidateExecutor {
	public static Map<String, String> checkValidate(String showErrorType,
			List<ValidatorConfigBean> valList, Map<String, String[]> map,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> error = null;
		if (valList != null && !valList.isEmpty()) {
			// 启动验证器
			for (ValidatorConfigBean val : valList) {
				ValidatorIF validator = null;
				if (!"".equals(val.getName())) {
					// 根据name属性实例化相关的验证器，如果找不到，就根据class属性实例化
					validator = ValidatorFactory.getValidator(val.getName());
					if (validator == null) {
						validator = (ValidatorIF) Class.forName(val.getClazz())
								.newInstance();
					}
				}

				if (validator != null) {
					// 执行验证
					Map<String, String> _map = validator.validate(val, map,
							request);
					if (_map != null) {
						if (error == null) {
							error = validator.validate(val, map, request);
						} else {
							error.putAll(_map);
						}
					}
				}
			}
			if (error != null) {
				ValidateExecutor.showValidateError(showErrorType, error,
						request, response);
			}
		}

		return error;
	}

	public static void showValidateError(String showErrorType,
			Map<String, String> error, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		if (error != null) {
			if (showErrorType == null || "".equals(showErrorType.trim())) {
				showErrorType = "alert";
			}
			if (ValidatorConstant.DEFAULT_LOC.equals(showErrorType)
					|| ValidatorConstant.ALERT_LOC
							.equalsIgnoreCase(showErrorType)) {
				StringBuilder emss = new StringBuilder();
				for (Entry<String, String> entrySet : error.entrySet()) {
					emss.append(entrySet.getValue()).append(" | ");
				}

				PrintWriter out = res.getWriter();
				out.print("<script>alert('"
						+ emss.toString()
						+ "');javascript:history.go(-1)</script><center></center>");
				out.flush();
				out.close();
			} else if (ValidatorConstant.AJAX_OUT_LOC
					.equalsIgnoreCase(showErrorType)) {
				StringBuilder emss = new StringBuilder();
				for (Entry<String, String> entrySet : error.entrySet()) {
					emss.append(entrySet.getValue()).append(" | ");
				}
				PrintWriter out = res.getWriter();
				out.print(emss.toString());
				out.flush();
				out.close();
			} else if (ValidatorConstant.DWZ_JSON_LOC
					.equalsIgnoreCase(showErrorType)) {
				StringBuilder emss = new StringBuilder();
				for (Entry<String, String> entrySet : error.entrySet()) {
					emss.append(entrySet.getValue()).append(" | ");
				}
				PrintWriter out = res.getWriter();
				out.print(new CallBackJson(emss.toString()));
				out.flush();
				out.close();
			} else if (ValidatorConstant.JAVASCRIPT_LOC
					.equalsIgnoreCase(showErrorType)) {
				StringBuilder emss = new StringBuilder();
				for (Entry<String, String> entrySet : error.entrySet()) {
					emss.append(entrySet.getValue()).append(" ; ");
				}
				PrintWriter out = res.getWriter();
				out.print("<script>" + emss.toString() + "</script>");
				out.flush();
				out.close();
			} else {
				// 如果是填写跳转页面的话
				if (showErrorType.startsWith("redirect:")) {
					String url = showErrorType.substring("redirect:".length());
					String location = url;
					int index = url.indexOf("?");
					if (index > 0) {
						location = URLEncoder.encode(url.substring(0, index),
								"utf-8") + "?" + url.substring(index + 1);
					}
					req.getSession(true).setAttribute("valError", error);
					// 客户端重定向
					res.sendRedirect(location);
				} else {
					String location = showErrorType;
					if (showErrorType.startsWith("forward:")) {
						location = showErrorType.substring("forward:".length());
					}
					req.setAttribute("valError", error);
					// 服务端跳转
					req.getRequestDispatcher(location).forward(req, res);
				}
			}
		}
		return;
	}
}
