package com.cfuture08.eweb4j.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cfuture08.eweb4j.cache.ActionConfigBeanCache;
import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.EWeb4JConfig;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.mvc.action.ActionExecutor;
import com.cfuture08.eweb4j.mvc.action.RequestMethodType;
import com.cfuture08.eweb4j.mvc.annotation.Result;
import com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ResultConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;
import com.cfuture08.eweb4j.mvc.interceptor.DoIntercept;
import com.cfuture08.eweb4j.mvc.util.HtmlCreator;
import com.cfuture08.eweb4j.mvc.validate.ValidateExecutor;

public class EWeb4JFilter implements Filter {
	public void init(FilterConfig arg0) throws ServletException {
		String mess = "---eweb4j starting";
		System.out.println(mess);
	}

	public void destroy() {
		String mess = "---eweb4j close";
		System.out.println(mess);
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) {

		PrintWriter out = null;
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			String message = EWeb4JConfig.start();
			if (message != null) {
				out = showError(response, message);
				return;
			}

			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			req.setCharacterEncoding("utf-8");
			res.setCharacterEncoding("utf-8");
			res.setContentType("text/html");
			res.setHeader("Charset", "utf-8");

			String uri = URLDecoder.decode(req.getRequestURI(), "utf-8");
			String contextPath = req.getContextPath();
			String _uri = uri.substring(1);

			if (contextPath != null && contextPath.trim().length() > 0)
				_uri = uri.replace(contextPath + "/", "");

			ConfigBean cb = (ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID);
			String debug = (cb == null ? "1" : cb.getMvc().getDebug());

			// 执行拦截器
			String interError = DoIntercept.intercept("before", _uri, req, res);
			if (interError != null) {
				// 记录拦截器错误信息日志
				interErrLog(req, _uri, debug, interError);
				return;
			}

			String reqMethod = getRestfulReqMethod(req);

			Map<String, List<?>> urlParamMap = null;
			// 匹配action
			if (ActionConfigBeanCache.containsKey(_uri)
					|| (urlParamMap = ActionConfigBeanCache.getByMatches(_uri,
							reqMethod)) != null) {
				// 记录action信息日志
				actionInfoLog(_uri, debug, reqMethod);
				// ------end log-------

				// 将request的请求参数转到另外一个map中去
				Map<String, String[]> map = copyReqParams(req);
				ActionConfigBean mvcBean = null;

				if (urlParamMap != null && urlParamMap.containsKey("mvcBean")) {
					// 根据Url配置的UrlParam获取参数值
					mvcBean = (ActionConfigBean) urlParamMap.get("mvcBean")
							.get(0);
					urlParamBind(map, urlParamMap);
				} else
					mvcBean = ActionConfigBeanCache.get(_uri);

				// 验证器
				boolean flag = handleValidator(mvcBean.getShowValErrorType(),
						mvcBean.getValidator(), map, req, res, debug);
				if (flag)
					return;
				else {
					// Action执行器
					if (ActionExecutor.checkAndDoAction(_uri, mvcBean, map,
							req, res) != null) {
						// 记录验证信息日志
						validatorLog(_uri, debug, reqMethod);
						return;
					}
				}
			}

			// 正常访问url
			normalReqLog(uri, debug);
			chain.doFilter(request, response);

		} catch (Exception e) {
			StringBuilder sb = new StringBuilder(e.toString());
			for (StackTraceElement ste : e.getStackTrace()) {
				sb.append("\n").append(ste.toString());
			}
			LogFactory.getMVCLogger("ERROR").write(sb.toString());
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	private PrintWriter showError(ServletResponse response, String message)
			throws IOException {
		PrintWriter out;
		out = response.getWriter();
		out.print(HtmlCreator.create(message));
		out.flush();
		out.close();
		out = null;
		String error = new StringBuilder("错误：").append(message).toString();
		LogFactory.getMVCLogger("ERROR").write(error);
		return out;
	}

	private void normalReqLog(String uri, String debug) {
		StringBuilder sb = new StringBuilder();
		sb.append("MVC:没有Action被执行，正常访问url:").append(uri);
		if ("true".equals(debug) || "1".equals(debug)) {
			System.out.println(sb.toString());
		}
		LogFactory.getMVCLogger("INFO").write(sb.toString());
	}

	private void validatorLog(String _uri, String debug, String reqMethod) {
		StringBuilder sb = new StringBuilder();
		sb.append("MVC:执行").append(_uri + "@" + reqMethod).append("[Action];");
		if ("true".equals(debug) || "1".equals(debug)) {
			System.out.println(sb.toString());
		}
		LogFactory.getMVCLogger("INFO").write(sb.toString());
	}

	private void urlParamBind(Map<String, String[]> map,
			Map<String, List<?>> urlParamMap) {
		@SuppressWarnings("unchecked")
		List<String> urlParamNames = (List<String>) urlParamMap
				.get("urlParamNames");
		@SuppressWarnings("unchecked")
		List<String> urlParamValues = (List<String>) urlParamMap
				.get("urlParamValues");
		if (urlParamMap != null && urlParamNames != null
				&& urlParamNames.size() > 0 && urlParamValues != null
				&& urlParamValues.size() > 0) {
			for (int i = 0; i < urlParamMap.get("urlParamNames").size(); i++) {
				String name = urlParamNames.get(i).substring(1);
//				System.out.println("param_name-->"+name);
				int lastIndex = name.length() - 1;
				if (lastIndex > 0) {
					String subName = name.substring(0, lastIndex);
//					System.out.println("subName-->"+subName);
//					System.out.println("val-->"+urlParamValues.get(i));
					map.put(subName, new String[] { urlParamValues.get(i) });
				}
			}
		}
	}

	private void actionInfoLog(String _uri, String debug, String reqMethod) {
		StringBuilder sb = new StringBuilder();
		sb.append("MVC:从缓存中找到").append(_uri + "@" + reqMethod)
				.append("[Action];");
		if ("true".equals(debug) || "1".equals(debug)) {
			System.out.println(sb.toString());
		}
		LogFactory.getMVCLogger("INFO").write(sb.toString());
	}

	private void interErrLog(HttpServletRequest req, String _uri, String debug,
			String interError) {
		StringBuilder sb = new StringBuilder();
		req.setAttribute("interError", interError);
		sb.append("MVC:拦截器拦截url：").append(_uri);
		sb.append("并输出错误信息:").append(interError);
		if ("true".equals(debug) || "1".equals(debug)) {
			System.out.println(sb.toString());
		}
		LogFactory.getMVCLogger("INFO").write(sb.toString());
	}

	// 将request的请求参数转到另外一个map中去
	private Map<String, String[]> copyReqParams(HttpServletRequest req)
			throws UnsupportedEncodingException {
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (@SuppressWarnings("unchecked")
		Iterator<Entry<String, String[]>> it = req.getParameterMap().entrySet()
				.iterator(); it.hasNext();) {
			Entry<String, String[]> e = it.next();
			String key = URLDecoder.decode(e.getKey(), "utf-8");
			String[] val = e.getValue();
			String[] values = new String[val.length];
			for (int i = 0; i < values.length; i++) {
				values[i] = URLDecoder.decode(val[i], "utf-8");
			}
			map.put(key, values);
		}
		return map;
	}

	public static List<ResultConfigBean> handleResult(Method m) {
		List<ResultConfigBean> list = null;
		Result reAnn = m.getAnnotation(Result.class);
		if (reAnn != null) {
			list = new ArrayList<ResultConfigBean>();
			for (int i = 0; i < reAnn.name().length; ++i) {
				ResultConfigBean r = new ResultConfigBean();
				r.setLocation(reAnn.value()[0]);
				r.setName(reAnn.name()[0]);
				r.setType(reAnn.type()[0]);
				list.add(r);
			}
		}
		return list;
	}

	private static boolean handleValidator(String showErrorType,
			List<ValidatorConfigBean> valList, Map<String, String[]> map,
			HttpServletRequest req, HttpServletResponse res, String debug)
			throws Exception {
		boolean flag = false;
		Map<String, String> error = ValidateExecutor.checkValidate(
				showErrorType, valList, map, req, res);
		if (error != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("MVC:验证器发现了错误").append(error);
			if ("true".equals(debug) || "1".equals(debug)) {
				System.out.println(sb.toString());
			}

			LogFactory.getMVCLogger("INFO").write(sb.toString());

			flag = true;
		}

		return flag;
	}

	/**
	 * getRestfulReqMethod
	 * 
	 * @param req
	 * @return
	 */
	private static String getRestfulReqMethod(HttpServletRequest req) {
		String reqMethod = req.getMethod();
		String _method = req.getParameter("_method");
		if (reqMethod.equalsIgnoreCase(RequestMethodType.POST)) {
			// POST
			if (_method != null) {
				if (RequestMethodType.PUT.equalsIgnoreCase(_method.trim())) {
					reqMethod = RequestMethodType.PUT;
				} else if (RequestMethodType.DELETE.equalsIgnoreCase(_method
						.trim())) {
					reqMethod = RequestMethodType.DELETE;
				}
			}
		}
		return reqMethod;
	}
}
