package com.cfuture08.eweb4j.mvc.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.ioc.IOC;
import com.cfuture08.eweb4j.ioc.annotation.Ioc;
import com.cfuture08.eweb4j.mvc.annotation.Param;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.Singleton;
import com.cfuture08.eweb4j.mvc.config.MVCConfigConstant;
import com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ResultConfigBean;
import com.cfuture08.eweb4j.mvc.config.cache.ActionClassCache;
import com.cfuture08.eweb4j.mvc.interceptor.DoIntercept;
import com.cfuture08.eweb4j.mvc.util.ParamUtil;
import com.cfuture08.util.JsonConverter;
import com.cfuture08.util.ReflectUtil;

public class ActionExecutor {
	public static String checkAndDoAction(String uri, ActionConfigBean mvcBean,
			Map<String, String[]> map, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		// 实例化pojo
		Class<?> clazz = ActionClassCache.get(mvcBean.getClazz());
		Object pojo = initPojo(clazz);
		// IOC注入对象到pojo中
		iocInject(pojo);
		// 将参数绑定到Action中去
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();
		ParamUtil.bindParam(pojo, map, req, res, out, session);

		Object re = null;
		String methodName = mvcBean.getMethod();

		if (methodName != null && methodName.length() > 0)
			// 执行方法
			re = excuteMethod(map, pojo, methodName, req, res, out, session);
		else if (IAction.class.isAssignableFrom(clazz)) {
			// struts2风格
			IAction action = (IAction) pojo;
			action.init(req, res, mvcBean);
			re = action.execute();
		}

		// Action执行之后
		String newInterError = DoIntercept.intercept("after", uri, req, res);
		if (newInterError != null) {
			logInterErr(uri, req, newInterError);// 日志
			return "interError";
		}

		return handleResult(re, mvcBean.getResult(), req, res);
	}

	private static void logInterErr(String uri, HttpServletRequest req,
			String newInterError) {
		StringBuilder _sb = new StringBuilder();
		req.setAttribute("interError", newInterError);
		_sb.append("MVC:AFTER拦截器拦截url：").append(uri);
		_sb.append("并输出错误信息:").append(newInterError);
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		String debug = (cb == null ? "1" : cb.getMvc().getDebug());
		if ("true".equals(debug) || "1".equals(debug)) {
			System.out.println(_sb.toString());
		}
		LogFactory.getMVCLogger("INFO").write(_sb.toString());
	}

	private static Object initPojo(Class<?> clazz)
			throws InstantiationException, IllegalAccessException {
		Object pojo;
		Annotation singletonAnn = clazz.getAnnotation(Singleton.class);
		if (singletonAnn != null) {
			pojo = SingleBeanCache.get(clazz);
			if (pojo == null) {
				pojo = clazz.newInstance();
				SingleBeanCache.add(clazz, pojo);
			}
		} else
			pojo = clazz.newInstance();

		return pojo;
	}

	// IOC，注入对象到pojo
	private static void iocInject(Object pojo) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		ReflectUtil ru = new ReflectUtil(pojo);
		Field[] fields = ru.getFields();
		if (fields != null) {
			for (Field f : fields) {
				Class<?> type = f.getType();
				Ioc ioc = f.getAnnotation(Ioc.class);
				if (ioc == null)
					continue;
				String beanId = "";
				if (ioc.value().trim().length() == 0)
					beanId = type.getSimpleName();
				else
					beanId = ioc.value();

				Method setter = ru.getSetter(f.getName());
				if (setter == null)
					continue;

				setter.invoke(pojo, IOC.getBean(beanId));
			}
		}
	}

	private static String excuteMethod(Map<String, String[]> paramMap,
			Object object, String methodName, HttpServletRequest req,
			HttpServletResponse res, PrintWriter out, HttpSession session)
			throws Exception {
		String re = null;
		Method[] methods = new ReflectUtil(object).getMethods(methodName);
		if (methods != null && methods.length > 0) {
			Method m = null;
			if (methods.length > 1) {
				// 如果含有两个或以上同名的方法，找第一个含有RequestMapping注解的方法
				for (Method mm : methods) {
					RequestMapping um = mm.getAnnotation(RequestMapping.class);
					if (um != null) {
						m = mm;
						break;
					}
				}
			} else
				m = methods[0];

			if (m != null) {
				Class<?>[] paramTypes = m.getParameterTypes();
				Annotation[][] paramAnns = m.getParameterAnnotations();
				if (paramTypes != null && paramTypes.length > 0) {
					Object[] params = new Object[paramTypes.length];
					for (int i = 0; i < paramTypes.length; ++i) {
						Param paramAnn = null;
						for (Annotation a : paramAnns[i]) {
							if (a != null)
								if (a.annotationType().isAssignableFrom(
										Param.class)) {
									paramAnn = (Param) a;
								}
							break;
						}

						String paramName = "";
						String paramInit = "";
						if (paramAnn != null) {
							paramName = paramAnn.value();
							paramInit = paramAnn.init();
						}
						Class<?> paramClass = paramTypes[i];
						if (HttpServletRequest.class
								.isAssignableFrom(paramClass))
							params[i] = req;
						else if (HttpServletResponse.class
								.isAssignableFrom(paramClass))
							params[i] = res;
						else if (HttpSession.class.isAssignableFrom(paramClass))
							params[i] = session;
						else if (ServletOutputStream.class
								.isAssignableFrom(paramClass))
							params[i] = out;
						else if (PrintWriter.class.isAssignableFrom(paramClass)) {
							try {
								params[i] = res.getWriter();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (Map.class.isAssignableFrom(paramClass))
							params[i] = paramMap;
						else {
							String[] paramValue = paramMap.get(paramName);
							if (paramValue == null)
								paramValue = new String[] { paramInit };

							if (Integer.class.isAssignableFrom(paramClass)
									|| int.class.isAssignableFrom(paramClass)) {
								if ("".equals(paramValue[0].trim()))
									paramValue[0] = "0";

								params[i] = Integer.parseInt(String
										.valueOf(paramValue[0]));

							} else if (Integer[].class
									.isAssignableFrom(paramClass)
									|| int[].class.isAssignableFrom(paramClass)) {
								Integer[] its = new Integer[paramValue.length];
								for (int j = 0; j < paramValue.length; ++j) {
									if ("".equals(paramValue[j].trim()))
										paramValue[j] = "0";

									its[j] = Integer.parseInt(paramValue[j]);

								}
								params[i] = its;
							} else if (Long.class.isAssignableFrom(paramClass)
									|| long.class.isAssignableFrom(paramClass)) {
								if ("".equals(paramValue[0].trim()))
									paramValue[0] = "0";

								params[i] = Long.parseLong(String
										.valueOf(paramValue[0]));
							} else if (Long[].class
									.isAssignableFrom(paramClass)
									|| long[].class
											.isAssignableFrom(paramClass)) {
								Long[] lgs = new Long[paramValue.length];
								for (int j = 0; j < paramValue.length; ++j) {
									if ("".equals(paramValue[j].trim()))
										paramValue[j] = "0";

									lgs[j] = Long.parseLong(paramValue[j]);
								}
								params[i] = lgs;
							} else if (Float.class.isAssignableFrom(paramClass)
									|| float.class.isAssignableFrom(paramClass)) {
								if ("".equals(paramValue[0].trim()))
									paramValue[0] = "0.0";

								params[i] = Float.parseFloat(String
										.valueOf(paramValue[0]));
							} else if (Float[].class
									.isAssignableFrom(paramClass)
									|| float[].class
											.isAssignableFrom(paramClass)) {
								Float[] its = new Float[paramValue.length];
								for (int j = 0; j < paramValue.length; ++j) {
									if ("".equals(paramValue[j].trim()))
										paramValue[j] = "0.0";

									its[j] = Float.parseFloat(paramValue[j]);
								}
								params[i] = its;
							} else if (Double.class
									.isAssignableFrom(paramClass)
									|| double.class
											.isAssignableFrom(paramClass)) {
								if ("".equals(paramValue[0].trim()))
									paramValue[0] = "0.0";

								params[i] = Double.parseDouble(String
										.valueOf(paramValue[0]));
							} else if (Double[].class
									.isAssignableFrom(paramClass)
									|| double[].class
											.isAssignableFrom(paramClass)) {
								Double[] dbs = new Double[paramValue.length];
								for (int j = 0; j < paramValue.length; ++j) {
									if ("".equals(paramValue[j].trim()))
										paramValue[j] = "0.0";

									dbs[j] = Double.parseDouble(paramValue[j]);
								}
								params[i] = dbs;
							} else if (String.class
									.isAssignableFrom(paramClass))
								params[i] = paramValue[0];
							else if (String[].class
									.isAssignableFrom(paramClass))
								params[i] = paramValue;
							else {
								// pojo
								// 将参数绑定到pojo中的属性去
								params[i] = ParamUtil.bindParam(paramName, null,
										paramClass.newInstance(), paramMap,
										req, res, out, session);
							}
						}
					}

					re = (String) m.invoke(object, params);// 带参数运行方法
				} else {
					re = (String) m.invoke(object);// 无参数运行方法
				}
			}
		}

		return re;
	}

	public static String handleResult(Object ret,
			List<ResultConfigBean> results, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String re = null;
		if (ret == null)
			re = IAction.AJAX;
		else if (String.class.isAssignableFrom(ret.getClass()))
			re = String.valueOf(ret);

		if (IAction.AJAX.equalsIgnoreCase(re)) {
			// ajax
			String ajax = (String) req
					.getAttribute(MVCConfigConstant.AJAX_ATTR);
			if (ajax != null) {
				PrintWriter out = res.getWriter();
				out.print(ajax);
				out.flush();
				out.close();
			}
			return MVCConfigConstant.AJAX_TYPE;
		} else if (IAction.JSON.equalsIgnoreCase(re)) {
			// json
			String json = (String) req
					.getAttribute(MVCConfigConstant.JSON_ATTR);
			if (json != null) {
				PrintWriter out = res.getWriter();
				out.print(json);
				out.flush();
				out.close();
			}
			return MVCConfigConstant.JSON_TYPE;
		} else if (IAction.JSON_OUT.equalsIgnoreCase(re)) {
			// jsonOut
			return MVCConfigConstant.JSON_TYPE;
		} else if (IAction.AJAX_OUT.equalsIgnoreCase(re)) {
			// ajaxOut
			return MVCConfigConstant.JSON_TYPE;
		} else {
			if (results == null || results.size() == 0) {
				// 客户端重定向
				if (re.startsWith("redirect:")) {
					String url = re.substring("redirect:".length());
					String location = url;
					int index = url.indexOf("?");
					if (index > 0) {
						location = URLEncoder.encode(url.substring(0, index),
								"utf-8") + "?" + url.substring(index + 1);
					}
					res.sendRedirect(location);
					return MVCConfigConstant.REDIRECT_TYPE;
				} else {
					String location = re;
					if (re.startsWith("forward:")) {
						location = re.substring("forward:".length());
					}

					// 服务端跳转
					req.getRequestDispatcher(location).forward(req, res);
					return MVCConfigConstant.FORWARD_TYPE;
				}
			}
			for (ResultConfigBean r : results) {
				if (re.equals(r.getName()) || "".equals(re)) {
					String type = r.getType();
					String location = r.getLocation();
					if (MVCConfigConstant.REDIRECT_TYPE.equalsIgnoreCase(type)) {
						// 客户端重定向
						int index = location.indexOf("?");
						if (index > 0) {
							location = URLEncoder.encode(
									location.substring(0, index), "utf-8")
									+ "?" + location.substring(index + 1);
						}
						res.sendRedirect(location);
						return MVCConfigConstant.REDIRECT_TYPE;
					} else if (MVCConfigConstant.FORWARD_TYPE
							.equalsIgnoreCase(type) || "".equals(type)) {
						// 服务端跳转
						req.getRequestDispatcher(location).forward(req, res);
						return MVCConfigConstant.FORWARD_TYPE;
					} else if (MVCConfigConstant.AJAX_TYPE
							.equalsIgnoreCase(type)) {
						// ajax
						return MVCConfigConstant.AJAX_TYPE;
					} else if (MVCConfigConstant.OUT_TYPE
							.equalsIgnoreCase(type)) {
						PrintWriter out = res.getWriter();
						out.print(location);
						out.flush();
						out.close();
						return MVCConfigConstant.OUT_TYPE;
					} else if (MVCConfigConstant.JSON_TYPE
							.equalsIgnoreCase(type)) {
						if (ret != null) {
							PrintWriter out = res.getWriter();
							out.print(JsonConverter.convert(ret));
							out.flush();
							out.close();
							return MVCConfigConstant.JSON_TYPE;
						}
					} else {
						return null;
					}
				}
			}
		}

		return null;
	}
}