package com.cfuture08.eweb4j.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean;
import com.cfuture08.eweb4j.mvc.util.UrlParamHandler;

/**
 * MVC组件缓存
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ActionConfigBeanCache {
	private static final HashMap<Object, ActionConfigBean> ht = new HashMap<Object, ActionConfigBean>();

	public static boolean containsKey(String beanID) {
		return ht.containsKey(beanID);
	}

	public static Map<String, List<?>> getByMatches(String aUrl,
			String reqMethod) {
		Map<String, List<?>> result = null;
		for (Iterator<Entry<Object, ActionConfigBean>> it = ht.entrySet()
				.iterator(); it.hasNext();) {

			Entry<Object, ActionConfigBean> entry = it.next();
			Object beanID = entry.getKey();
			ActionConfigBean mvcBean = entry.getValue();
			if (String.class.isAssignableFrom(beanID.getClass())) {
				// 如果是String
				String regex = String.valueOf(beanID).replace(
						"@" + mvcBean.getReqMethod(), "");
				if (regex.contains("{") || regex.contains("}"))
					continue;

				if (aUrl.endsWith("/"))
					aUrl = aUrl.substring(0, aUrl.length() - 1);

				if (regex.endsWith("/"))
					regex = regex.substring(0, regex.length() - 1);

				String[] methods = mvcBean.getReqMethod().split("\\|");
				boolean checkMethod = false;
				for (String m : methods) {
					if (reqMethod.equalsIgnoreCase(m.trim())) {
						checkMethod = true;
						break;
					}
				}
				if (regex != null && checkMethod && aUrl.matches(regex)) {
					result = new HashMap<String, List<?>>();
					// 1.hello/{name}/test/{id}
					// 2.{"hello/","{name}","/test/{id}"}
					String urlMapping = mvcBean.getName();
					String reqUrl = aUrl;
					// 如果urlMapping的开头是“/”要去掉
					if (urlMapping.startsWith("/"))
						urlMapping = urlMapping.substring(1);

					Pattern p = Pattern
							.compile("\\{[a-zA-Z\\_\u4e00-\u9fa5]+\\}");
					Matcher m = p.matcher(urlMapping);

					List<String> urlParamNames = new ArrayList<String>();
					List<String> urlParamValues = new ArrayList<String>();
					while (m.find()) {
						String g = m.group();
//						System.out.println("g->" + g);
						String[] regexSplit = urlMapping
								.split("\\{[a-zA-Z\\_\u4e00-\u9fa5]+\\}");
						String paramVal = UrlParamHandler.matchersUrlParam(
								reqUrl, regexSplit);

//						System.out.println("pV->" + paramVal);
						if (paramVal != null) {
							urlParamValues.add(paramVal);
							urlParamNames.add(g);
							reqUrl = new String(reqUrl.substring(reqUrl
									.indexOf(paramVal) + paramVal.length()));
						}

						urlMapping = new String(urlMapping.substring(urlMapping
								.indexOf(g) + g.length()));
//						System.out.println("urlMap.sub-->" + urlMapping);
//						System.out.println("reqUrl-->"+reqUrl);
					}

					if (urlParamNames.size() > 0 && urlParamValues.size() > 0) {
						result.put("urlParamNames", urlParamNames);
						result.put("urlParamValues", urlParamValues);
					}

					List<ActionConfigBean> mvcBeanList = new ArrayList<ActionConfigBean>();
					mvcBeanList.add(mvcBean);
					result.put("mvcBean", mvcBeanList);
					break;
				}
			}
		}

		return result;
	}

	public static boolean containsKey(Class<?> clazz) {
		return ht.containsKey(clazz);
	}

	public static void add(String beanID, ActionConfigBean o) {
		if (beanID != null && o != null) {
			ConfigBean cb = (ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID);
			String debug = (cb == null ? "true" : cb.getMvc().getDebug());
			String info = null;

			if (!ht.containsKey(beanID)) {
				ht.put(beanID, o);
				info = "ActionConfigBeanCache:add...finished..." + beanID;
			} else {
				info = "ActionConfigBeanCache:add...重复的aciton名字..." + beanID;
			}
			if ("1".equals(debug) || "true".equals(debug)) {
				System.out.println(info);
			}
			LogFactory.getIOCLogger("INFO").write(info);
		}
	}

	public static void add(Class<?> clazz, ActionConfigBean o) {
		if (clazz != null && o != null) {
			ConfigBean cb = (ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID);
			String debug = (cb == null ? "true" : cb.getMvc().getDebug());
			String info = null;
			if (!ht.containsKey(clazz)) {
				ht.put(clazz, o);
				info = "ActionConfigBeanCache:add...finished..." + clazz;
			} else {
				info = "ActionConfigBeanCache:add...重复的aciton类..." + clazz;
			}
			if ("1".equals(debug) || "true".equals(debug)) {
				System.out.println(info);
			}
			LogFactory.getIOCLogger("INFO").write(info);
		}
	}

	public static ActionConfigBean get(String beanID) {
		ActionConfigBean o = null;
		if (beanID != null) {
			if (ht.containsKey(beanID)) {
				o = ht.get(beanID);
			}
		}
		return o;
	}

	public static ActionConfigBean get(Class<?> clazz) {
		ActionConfigBean o = null;
		if (clazz != null) {
			if (ht.containsKey(clazz)) {
				o = ht.get(clazz);
			}
		}

		return o;
	}

	public static void clear() {
		if (!ht.isEmpty()) {
			ht.clear();
		}
	}

	public static void main(String[] args) {
		String[] methods = "GET|POST".split("\\|");
		boolean checkMethod = false;
		for (String m : methods) {
			if (m.equalsIgnoreCase("POST")) {
				checkMethod = true;
				break;
			}
		}
		System.out.println(checkMethod);
	}

}
