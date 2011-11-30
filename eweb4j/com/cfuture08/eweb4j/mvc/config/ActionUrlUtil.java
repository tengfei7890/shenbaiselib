package com.cfuture08.eweb4j.mvc.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cfuture08.eweb4j.mvc.annotation.Param;
import com.cfuture08.util.ClassUtil;
import com.cfuture08.util.ReflectUtil;

public class ActionUrlUtil {
	/**
	 * 匹配url中变量部分{},并将其转换成正则表达式 将url中的{xxx}转化为正则\\d+或者\\[a-zA-Z\\_]+
	 * 
	 * @param m
	 *            控制器中的actoin方法
	 * @param actionName
	 *            控制器的@RequestMapping的value值+方法中@RequestMapping的value值
	 * @return
	 * @throws Exception
	 */
	public static String mathersUrlMapping(Method m, String actionName,
			Class<?> cls) throws Exception {
		List<String> paramNames = new ArrayList<String>();
		List<Class<?>> paramClasses = new ArrayList<Class<?>>();

		// 方法参数变量
		Class<?>[] paramTypes = m.getParameterTypes();
		Annotation[][] paramAnns = m.getParameterAnnotations();
		loadParam(paramTypes, paramAnns, paramNames, paramClasses);

		// Action类实例属性变量
		ReflectUtil ru = new ReflectUtil(cls);
		Field[] fields = ru.getFields();
		loadParam(fields, paramNames, paramClasses);

		if (paramClasses != null && paramClasses.size() > 0)
			actionName = urlParamToRegex(actionName, paramNames, paramClasses);

		if (actionName.endsWith("/"))
			actionName = actionName.substring(0, actionName.length() - 1);

		if (actionName.startsWith("/"))
			actionName = actionName.substring(1);

		return actionName;
	}

	private static String urlParamToRegex(String actionName,
			List<String> paramNames, List<Class<?>> paramClasses) {
		// 每成功读取一个action配置信息，就保存到内存中
		Pattern pattern = Pattern.compile("\\{[0-9a-zA-Z\\_\u4e00-\u9fa5]+\\}");
		Matcher matcher = pattern.matcher(actionName);
		while (matcher.find()) {
			// 如果url中含有{xxx}变量，将其变成正则
			String g = matcher.group();
			for (int k = 0; k < paramClasses.size(); k++) {
				boolean isBreak = false;
				if (g.equals("{" + paramNames.get(k) + "}")) {
					String regex = null;
					if (Number.class.isAssignableFrom(paramClasses.get(k))
							|| int.class.isAssignableFrom(paramClasses.get(k))
							|| long.class.isAssignableFrom(paramClasses.get(k)))
						regex = "\\d+";
					else
						regex = "[a-zA-Z\\_\u4e00-\u9fa5]+";

					actionName = actionName.replace(g, regex);
					isBreak = true;
					break;
				}

				if (isBreak) {
					break;
				}
			}
		}
		return actionName;
	}

	private static void loadParam(Field[] fields, List<String> paramNames,
			List<Class<?>> paramClasses) throws InstantiationException,
			IllegalAccessException {
		for (int j = 0; j < fields.length; j++) {
			Field f = fields[j];
			Class<?> cls = f.getType();
			if (ClassUtil.isPojo(cls)) {
				pojoFieldToUrlParam(paramNames, paramClasses, cls);
			} else {
				paramNames.add(f.getName());
				paramClasses.add(cls);
			}
		}
	}

	private static void loadParam(Class<?>[] paramTypes,
			Annotation[][] paramAnns, List<String> paramNames,
			List<Class<?>> paramClasses) throws InstantiationException,
			IllegalAccessException {
		for (int j = 0; j < paramTypes.length; j++) {
			if (paramAnns[j].length > 0) {
				for (Annotation a : paramAnns[j]) {
					if (a != null
							&& Param.class.isAssignableFrom(a.annotationType())) {
						Param rp = (Param) a;
						paramNames.add(rp.value());
						paramClasses.add(paramTypes[j]);
						break;
					}
				}
			} else if (ClassUtil.isPojo(paramTypes[j])) {
				Class<?> cls = paramTypes[j];
				pojoFieldToUrlParam(paramNames, paramClasses, cls);
			}
		}
	}

	private static void pojoFieldToUrlParam(List<String> paramNames,
			List<Class<?>> paramClasses, Class<?> cls)
			throws InstantiationException, IllegalAccessException {
		ReflectUtil _ru = new ReflectUtil(cls);
		String[] fieldNames = _ru.getFieldsName();
		Field[] fields = _ru.getFields();
		paramNames.addAll(Arrays.asList(fieldNames));
		for (java.lang.reflect.Field f : fields) {
			paramClasses.add(f.getType());
		}
	}
}
